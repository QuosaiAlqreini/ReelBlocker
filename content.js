(function () {
    'use strict';

    function checkAndRedirectReels() {
        const path = window.location.pathname;
        if (path.startsWith('/reels/') || path.startsWith('/reel/')) {
            console.log('[Anti-Reels] Intercepted Reels URL. Redirecting to Direct Messages...');
            window.location.replace('/direct/inbox/');
        }
    }

    // Check on startup
    checkAndRedirectReels();

    // Intercept client-side navigation
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

    function scrubReelsFromDOM() {
        const reelLinks = document.querySelectorAll('a[href*="/reels/"], a[href*="/reel/"]');
        reelLinks.forEach(link => {
            const navItem = link.closest('li, div[role="tab"], div[role="listitem"]');
            if (navItem) {
                navItem.style.display = 'none';
            } else {
                link.style.display = 'none';
            }
        });
    }

    const observer = new MutationObserver(() => {
        scrubReelsFromDOM();
        checkAndRedirectReels();
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            scrubReelsFromDOM();
            observer.observe(document.body, { childList: true, subtree: true });
        });
    } else {
        scrubReelsFromDOM();
        observer.observe(document.body, { childList: true, subtree: true });
    }
})();
