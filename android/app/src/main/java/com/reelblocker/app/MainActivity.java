package com.reelblocker.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;
    private static final int FILE_CHOOSER_RESULT_CODE = 1001;

    private static final String DM_URL = "https://www.instagram.com/direct/inbox/";

    private static final String ANTI_REELS_CSS =
            "a[href*=\\\"/reels/\\\"], a[href*=\\\"/reel/\\\"]," +
            "div[role=\\\"navigation\\\"] a[href*=\\\"/reels/\\\"]," +
            "div[role=\\\"tablist\\\"] a[href*=\\\"/reels/\\\"]," +
            "article:has(a[href*=\\\"/reel/\\\"])," +
            "article:has(a[href*=\\\"/reels/\\\"]) {" +
            "   display: none !important;" +
            "   visibility: hidden !important;" +
            "   pointer-events: none !important;" +
            "}";

    private static final String INJECT_SCRIPT =
            "(function() {" +
            "   var style = document.getElementById('anti-reels-style');" +
            "   if (!style) {" +
            "       style = document.createElement('style');" +
            "       style.id = 'anti-reels-style';" +
            "       style.innerHTML = '" + ANTI_REELS_CSS + "';" +
            "       document.head.appendChild(style);" +
            "   }" +
            "   function check() {" +
            "       if (location.pathname.startsWith('/reels/') || location.pathname.startsWith('/reel/')) {" +
            "           location.replace('" + DM_URL + "');" +
            "       }" +
            "   }" +
            "   check();" +
            "   window.addEventListener('popstate', check);" +
            "})();";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        // Configure Cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // Configure WebSettings
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Set modern mobile User-Agent
        String defaultUa = settings.getUserAgentString();
        settings.setUserAgentString(defaultUa.replace("; wv", ""));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("/reels/") || url.contains("/reel/")) {
                    view.loadUrl(DM_URL);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("/reels/") || url.contains("/reel/")) {
                    view.loadUrl(DM_URL);
                } else {
                    view.evaluateJavascript(INJECT_SCRIPT, null);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript(INJECT_SCRIPT, null);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (MainActivity.this.filePathCallback != null) {
                    MainActivity.this.filePathCallback.onReceiveValue(null);
                }
                MainActivity.this.filePathCallback = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
                } catch (Exception e) {
                    MainActivity.this.filePathCallback = null;
                    return false;
                }
                return true;
            }
        });

        webView.loadUrl(DM_URL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (filePathCallback != null) {
                Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                filePathCallback.onReceiveValue(results);
                filePathCallback = null;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
