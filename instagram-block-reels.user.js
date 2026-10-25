// ==UserScript==
// @name         Instagram Anti-Reels (DMs & Messages Only)
// @namespace    https://github.com/QuosaiAlqreini/ReelBlocker
// @version      1.1.0
// @description  Blocks Instagram Reels, removes the Reels tab and video feeds while keeping Direct Messages (DMs) completely functional on mobile & desktop browsers.
// @author       QuosaiAlqreini
// @match        https://*.instagram.com/*
// @grant        none
// @run-at       document-start
// ==/UserScript==

(function () {
    'use strict';

    const DM_URL = 'https://www.instagram.com/direct/inbox/';

    function checkAndRedirectReels() {
        const path = window.location.pathname.toLowerCase();
        if (path === '/reels' || path.startsWith('/reels/') || path === '/reel' || path.startsWith('/reel/')) {
            console.log('[Anti-Reels] Intercepted Reels URL (' + path + '). Redirecting to Direct Messages...');
            window.location.replace(DM_URL);
        }
    }

    checkAndRedirectReels();

    const originalPushState = history.pushState;
    history.pushState = function () {
        originalPushState.apply(this, arguments);
        checkAndRedirectReels();
        scrubReelsFromDOM();
    };

    const originalReplaceState = history.replaceState;
    history.replaceState = function () {
        originalReplaceState.apply(this, arguments);
        checkAndRedirectReels();
        scrubReelsFromDOM();
    };

    window.addEventListener('popstate', function () {
        checkAndRedirectReels();
        scrubReelsFromDOM();
    });

    document.addEventListener('click', function (e) {
        const target = e.target && e.target.closest ? e.target.closest('a, div[role="tab"], div[role="button"], div[role="link"]') : null;
        if (!target) return;

        const href = target.getAttribute('href') || (target.querySelector('a') ? target.querySelector('a').getAttribute('href') : '') || '';
        const hasReelAria = target.querySelector('svg[aria-label*="Reel" i], svg[aria-label*="reel" i]') || 
                            (target.getAttribute('aria-label') && target.getAttribute('aria-label').toLowerCase().includes('reel'));

        if ((href && (href.toLowerCase().includes('/reels') || href.toLowerCase().includes('/reel'))) || hasReelAria) {
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            console.log('[Anti-Reels] Blocked click navigation to Reels. Redirecting to Direct Messages...');
            window.location.replace(DM_URL);
        }
    }, true);

    const css = `
        a[href*="/reels"],
        a[href*="/reel"],
        div[role="navigation"] a[href*="/reels"],
        div[role="navigation"] a[href*="/reel"],
        div[role="tablist"] a[href*="/reels"],
        div[role="tablist"] a[href*="/reel"],
        div[role="tablist"] div:has(svg[aria-label*="Reel" i]),
        div[role="navigation"] div:has(svg[aria-label*="Reel" i]),
        a:has(svg[aria-label*="Reel" i]),
        div[role="tab"]:has(svg[aria-label*="Reel" i]) {
            display: none !important;
            visibility: hidden !important;
            opacity: 0 !important;
            pointer-events: none !important;
            max-height: 0 !important;
            overflow: hidden !important;
        }

        article:has(a[href*="/reel"]),
        article:has(a[href*="/reels"]),
        article:has(svg[aria-label*="Reel" i]),
        div:has(> a[href*="/reels"]) {
            display: none !important;
            visibility: hidden !important;
            max-height: 0 !important;
            overflow: hidden !important;
        }

        a[href*="/reel/"],
        a[href*="/reel"] {
            display: none !important;
        }

        a[href*="/direct/inbox"] {
            display: flex !important;
        }
    `;

    const styleEl = document.createElement('style');
    styleEl.id = 'anti-reels-style';
    styleEl.textContent = css;

    const parent = document.head || document.documentElement;
    if (parent) {
        parent.appendChild(styleEl);
    } else {
        document.addEventListener('DOMContentLoaded', () => {
            (document.head || document.documentElement).appendChild(styleEl);
        });
    }

    function scrubReelsFromDOM() {
        try {
            const reelLinks = document.querySelectorAll('a[href*="/reels"], a[href*="/reel"]');
            reelLinks.forEach(link => {
                const item = link.closest('li, div[role="tab"], div[role="listitem"], article, div[data-pressable-container="true"]');
                if (item) {
                    item.style.setProperty('display', 'none', 'important');
                } else {
                    link.style.setProperty('display', 'none', 'important');
                }
            });

            const svgs = document.querySelectorAll('svg[aria-label*="Reel" i], svg[aria-label*="reel" i], svg[aria-label="Clips"]');
            svgs.forEach(svg => {
                const navParent = svg.closest('a, div[role="tab"], div[role="button"], div[role="link"], li, article');
                if (navParent) {
                    navParent.style.setProperty('display', 'none', 'important');
                }
            });
        } catch (err) {}
    }

    const observer = new MutationObserver(() => {
        scrubReelsFromDOM();
        checkAndRedirectReels();
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            scrubReelsFromDOM();
            observer.observe(document.body || document.documentElement, { childList: true, subtree: true });
        });
    } else {
        scrubReelsFromDOM();
        observer.observe(document.body || document.documentElement, { childList: true, subtree: true });
    }
})();
