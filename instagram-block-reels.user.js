// ==UserScript==
// @name         Instagram Anti-Reels (DMs & Messages Only)
// @namespace    https://github.com/antigravity
// @version      1.0.0
// @description  Blocks Instagram Reels, removes the Reels tab and video feeds while keeping Direct Messages (DMs) completely functional on mobile & desktop browsers.
// @author       Antigravity
// @match        https://*.instagram.com/*
// @grant        none
// @run-at       document-start
// ==/UserScript==

(function () {
    'use strict';

    // 1. Immediately intercept URL navigation to Reels
    function checkAndRedirectReels() {
        const path = window.location.pathname;
        if (path.startsWith('/reels/') || path.startsWith('/reel/')) {
            console.log('[Anti-Reels] Reels detected! Redirecting to Direct Messages...');
            // Redirect to messages inbox
            window.location.replace('/direct/inbox/');
        }
    }

    // Run check on initial load
    checkAndRedirectReels();

    // Intercept pushState & replaceState for single-page app (SPA) navigation
    const originalPushState = history.pushState;
    history.pushState = function () {
        originalPushState.apply(this, arguments);
        checkAndRedirectReels();
    };

    const originalReplaceState = history.replaceState;
    history.replaceState = function () {
        originalReplaceState.apply(this, arguments);
        checkAndRedirectReels();
    };

    window.addEventListener('popstate', checkAndRedirectReels);

    // 2. Inject CSS to hide all Reels elements instantly (before render to prevent flickering)
    const css = `
        /* Hide Reels tab in bottom navigation bar and sidebar */
        a[href*="/reels/"],
        a[href*="/reel/"],
        div[role="navigation"] a[href*="/reels/"],
        div[role="tablist"] a[href*="/reels/"] {
            display: none !important;
            visibility: hidden !important;
            opacity: 0 !important;
            pointer-events: none !important;
        }

        /* Hide Reels sections / trays in Home feed */
        article:has(a[href*="/reel/"]),
        article:has(a[href*="/reels/"]),
        div:has(> a[href*="/reels/"]) {
            display: none !important;
        }

        /* Hide Reels icon in Explore grid if detectable */
        a[href*="/reel/"] {
            display: none !important;
        }

        /* Ensure Messages navigation item is highlighted and accessible */
        a[href*="/direct/inbox/"] {
            display: flex !important;
        }

        /* Quick-access floating DM button on mobile (optional helper) */
        #anti-reels-quick-dm {
            position: fixed;
            bottom: 60px;
            right: 16px;
            background: #0095f6;
            color: #ffffff;
            border-radius: 50%;
            width: 44px;
            height: 44px;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            z-index: 999999;
            text-decoration: none;
            transition: transform 0.2s;
        }
        #anti-reels-quick-dm:active {
            transform: scale(0.92);
        }
    `;

    const styleEl = document.createElement('style');
    styleEl.id = 'anti-reels-style';
    styleEl.textContent = css;

    if (document.head) {
        document.head.appendChild(styleEl);
    } else {
        document.addEventListener('DOMContentLoaded', () => {
            document.head.appendChild(styleEl);
        });
    }

    // 3. MutationObserver to remove dynamically injected reel containers
    function scrubReelsFromDOM() {
        // Target any reels anchors
        const reelLinks = document.querySelectorAll('a[href*="/reels/"], a[href*="/reel/"]');
        reelLinks.forEach(link => {
            // Check if it's in navigation or a feed item
            const navItem = link.closest('li, div[role="tab"], div[role="listitem"]');
            if (navItem) {
                navItem.style.display = 'none';
            } else {
                link.style.display = 'none';
            }
        });
    }

    // Observe changes to clean up dynamic feed items
    const observer = new MutationObserver(() => {
        scrubReelsFromDOM();
        checkAndRedirectReels();
    });

    document.addEventListener('DOMContentLoaded', () => {
        scrubReelsFromDOM();
        observer.observe(document.body, { childList: true, subtree: true });
    });
})();
