package com.reelblocker.app;

/**
 * Bulletproof Reels filtering, CSS hiding, and click/navigation interception.
 */
public final class ReelsBlockerLogic {

    private ReelsBlockerLogic() {}

    public static final String DM_URL = "https://www.instagram.com/direct/inbox/";

    public static boolean isReelUrl(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        // Matches /reels, /reels/, /reel, /reel/, etc.
        return lower.contains("/reels") || lower.contains("/reel");
    }

    public static boolean isInstagramUrl(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        return lower.contains("instagram.com");
    }

    public static final String ANTI_REELS_CSS =
            "a[href*='/reels'], a[href*='/reel'], " +
            "div[role='navigation'] a[href*='/reels'], " +
            "div[role='navigation'] a[href*='/reel'], " +
            "div[role='tablist'] a[href*='/reels'], " +
            "div[role='tablist'] a[href*='/reel'], " +
            "div[role='tablist'] div:has(svg[aria-label*='Reel' i]), " +
            "div[role='navigation'] div:has(svg[aria-label*='Reel' i]), " +
            "a:has(svg[aria-label*='Reel' i]), " +
            "div[role='tab']:has(svg[aria-label*='Reel' i]), " +
            "article:has(a[href*='/reel']), " +
            "article:has(a[href*='/reels']), " +
            "article:has(svg[aria-label*='Reel' i]), " +
            "div:has(> a[href*='/reels']) {" +
            "   display: none !important;" +
            "   visibility: hidden !important;" +
            "   opacity: 0 !important;" +
            "   pointer-events: none !important;" +
            "   max-height: 0 !important;" +
            "   overflow: hidden !important;" +
            "}" +
            "a[href*='/direct/inbox'] {" +
            "   display: flex !important;" +
            "}";

    public static final String INJECT_SCRIPT =
            "(function() {" +
            "   function injectStyle() {" +
            "       if (document.getElementById('anti-reels-style')) return;" +
            "       var style = document.createElement('style');" +
            "       style.id = 'anti-reels-style';" +
            "       style.textContent = \"" + ANTI_REELS_CSS + "\";" +
            "       var parent = document.head || document.documentElement || document.body;" +
            "       if (parent) parent.appendChild(style);" +
            "   }" +
            "   injectStyle();" +
            "   function checkAndRedirect() {" +
            "       var p = window.location.pathname.toLowerCase();" +
            "       if (p === '/reels' || p.startsWith('/reels/') || p === '/reel' || p.startsWith('/reel/')) {" +
            "           window.location.replace('" + DM_URL + "');" +
            "       }" +
            "   }" +
            "   checkAndRedirect();" +
            "   function scrub() {" +
            "       try {" +
            "           var links = document.querySelectorAll('a[href*=\"/reels\"], a[href*=\"/reel\"]');" +
            "           for (var i = 0; i < links.length; i++) {" +
            "               var nav = links[i].closest('li, div[role=\"tab\"], div[role=\"listitem\"], article, div[data-pressable-container=\"true\"]');" +
            "               if (nav) { nav.style.setProperty('display', 'none', 'important'); }" +
            "               else { links[i].style.setProperty('display', 'none', 'important'); }" +
            "           }" +
            "           var svgs = document.querySelectorAll('svg[aria-label*=\"Reel\" i], svg[aria-label*=\"reel\" i], svg[aria-label=\"Clips\"]');" +
            "           for (var j = 0; j < svgs.length; j++) {" +
            "               var navParent = svgs[j].closest('a, div[role=\"tab\"], div[role=\"button\"], div[role=\"link\"], li, article');" +
            "               if (navParent) { navParent.style.setProperty('display', 'none', 'important'); }" +
            "           }" +
            "       } catch(e) {}" +
            "   }" +
            "   if (!window.__antiReelsHooked) {" +
            "       window.__antiReelsHooked = true;" +
            "       var origPush = history.pushState;" +
            "       history.pushState = function() {" +
            "           origPush.apply(this, arguments);" +
            "           checkAndRedirect();" +
            "           scrub();" +
            "       };" +
            "       var origReplace = history.replaceState;" +
            "       history.replaceState = function() {" +
            "           origReplace.apply(this, arguments);" +
            "           checkAndRedirect();" +
            "           scrub();" +
            "       };" +
            "       window.addEventListener('popstate', function() {" +
            "           checkAndRedirect();" +
            "           scrub();" +
            "       });" +
            "       document.addEventListener('click', function(e) {" +
            "           var target = e.target && e.target.closest ? e.target.closest('a, div[role=\"tab\"], div[role=\"button\"], div[role=\"link\"]') : null;" +
            "           if (!target) return;" +
            "           var href = target.getAttribute('href') || (target.querySelector('a') ? target.querySelector('a').getAttribute('href') : '') || '';" +
            "           var hasReelAria = target.querySelector('svg[aria-label*=\"Reel\" i], svg[aria-label*=\"reel\" i]') || (target.getAttribute('aria-label') && target.getAttribute('aria-label').toLowerCase().indexOf('reel') !== -1);" +
            "           if ((href && (href.toLowerCase().indexOf('/reels') !== -1 || href.toLowerCase().indexOf('/reel') !== -1)) || hasReelAria) {" +
            "               e.preventDefault();" +
            "               e.stopPropagation();" +
            "               e.stopImmediatePropagation();" +
            "               window.location.replace('" + DM_URL + "');" +
            "           }" +
            "       }, true);" +
            "       var obs = new MutationObserver(function() {" +
            "           injectStyle();" +
            "           scrub();" +
            "           checkAndRedirect();" +
            "       });" +
            "       var body = document.body || document.documentElement;" +
            "       if (body) { obs.observe(body, { childList: true, subtree: true }); }" +
            "   }" +
            "   scrub();" +
            "})();";
}
