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

    // Check immediately
    checkAndRedirectReels();

    // Intercept client-side SPA navigation
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

    // Capture-phase click interceptor (blocks clicking before Meta React router can navigate)
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

    function scrubReelsFromDOM() {
        try {
            // Target all reels anchors
            const reelLinks = document.querySelectorAll('a[href*="/reels"], a[href*="/reel"]');
            reelLinks.forEach(link => {
                const item = link.closest('li, div[role="tab"], div[role="listitem"], article, div[data-pressable-container="true"]');
                if (item) {
                    item.style.setProperty('display', 'none', 'important');
                } else {
                    link.style.setProperty('display', 'none', 'important');
                }
            });

            // Target SVG icons representing reels
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
