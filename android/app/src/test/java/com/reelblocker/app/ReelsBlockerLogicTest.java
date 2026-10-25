package com.reelblocker.app;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests validating Reels detection, safe URL handling, and script safety across all devices.
 */
public class ReelsBlockerLogicTest {

    @Test
    public void testReelUrlDetection() {
        assertTrue("Direct reel video link must be detected",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/reel/C8kL4nXPx9y/"));

        assertTrue("Reels tab URL without trailing slash must be detected",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/reels"));

        assertTrue("Reels tab URL with trailing slash must be detected",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/reels/"));

        assertTrue("Reels subpath URL must be detected",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/reels/videos/explore"));

        assertTrue("Case insensitive check should work",
                ReelsBlockerLogic.isReelUrl("HTTPS://WWW.INSTAGRAM.COM/REEL/XYZ123/"));
    }

    @Test
    public void testNonReelUrlsAllowed() {
        assertFalse("Direct messages inbox must NOT be flagged as reel",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/direct/inbox/"));

        assertFalse("Direct messages inbox without slash must NOT be flagged as reel",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/direct/inbox"));

        assertFalse("Direct messages chat thread must NOT be flagged as reel",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/direct/t/178414000000000/"));

        assertFalse("Standard feed post must NOT be flagged as reel",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/p/C99XYZ123/"));

        assertFalse("User profile must NOT be flagged as reel",
                ReelsBlockerLogic.isReelUrl("https://www.instagram.com/instagram/"));

        assertFalse("Null URL must return false gracefully",
                ReelsBlockerLogic.isReelUrl(null));
    }

    @Test
    public void testInstagramUrlRecognition() {
        assertTrue("Instagram domain should be recognized",
                ReelsBlockerLogic.isInstagramUrl("https://www.instagram.com/direct/inbox/"));

        assertTrue("Instagram subdomain should be recognized",
                ReelsBlockerLogic.isInstagramUrl("https://instagram.com/"));

        assertFalse("External link (e.g. YouTube) should not be considered Instagram",
                ReelsBlockerLogic.isInstagramUrl("https://www.youtube.com/watch?v=123"));

        assertFalse("External link (e.g. Google) should not be considered Instagram",
                ReelsBlockerLogic.isInstagramUrl("https://maps.google.com/"));

        assertFalse("Null URL must return false gracefully",
                ReelsBlockerLogic.isInstagramUrl(null));
    }

    @Test
    public void testInjectionScriptIntegrity() {
        assertNotNull("Injection script must not be null", ReelsBlockerLogic.INJECT_SCRIPT);
        assertTrue("Script must contain DM URL redirect target",
                ReelsBlockerLogic.INJECT_SCRIPT.contains(ReelsBlockerLogic.DM_URL));
        assertTrue("Script must include pushState interception",
                ReelsBlockerLogic.INJECT_SCRIPT.contains("history.pushState"));
        assertTrue("Script must include replaceState interception",
                ReelsBlockerLogic.INJECT_SCRIPT.contains("history.replaceState"));
        assertTrue("Script must include MutationObserver",
                ReelsBlockerLogic.INJECT_SCRIPT.contains("MutationObserver"));
        assertTrue("Script must include click interception",
                ReelsBlockerLogic.INJECT_SCRIPT.contains("document.addEventListener('click'"));
    }
}
