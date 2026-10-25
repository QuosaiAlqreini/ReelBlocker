# 🛡️ ReelBlocker

> **Block Instagram Reels, eliminate distractions, and keep your Direct Messages (DMs) 100% accessible on your phone.**

[![Install Web App](https://img.shields.io/badge/Install_App-Live_PWA-brightgreen.svg?logo=googlechrome)](https://quosaialqreini.github.io/ReelBlocker/)
[![Download Android APK](https://img.shields.io/badge/Download-Android_APK-blue.svg?logo=android)](https://github.com/QuosaiAlqreini/ReelBlocker/releases/latest/download/ReelBlocker.apk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

ReelBlocker eliminates the addictive Reels algorithm, bottom navigation tab, and explore video feeds from Instagram while keeping your **Direct Messages (DMs)**, message notifications, and chat features fully available.

---

## 📲 How to Install ReelBlocker on Your Phone

### ⚡ Method 1: Instant App Installation (iPhone & Android) — Recommended
You can install ReelBlocker directly onto your phone screen without any third-party app stores:

1. Open this link on your phone: **[https://quosaialqreini.github.io/ReelBlocker/](https://quosaialqreini.github.io/ReelBlocker/)**
2. Follow the 1-tap prompt for your phone:
   - **🤖 On Android (Chrome / Brave / Samsung Internet):** Tap the **"Install App on Phone"** button that pops up.
   - **🍎 On iPhone (Safari):** Tap the **Share** button (box with an arrow pointing up at the bottom) ➔ tap **"Add to Home Screen"** ➔ tap **Add**.
3. **Done!** ReelBlocker will appear on your phone's home screen as a standalone app that launches directly into your distraction-free Instagram Direct Messages.

---

### 🤖 Method 2: Android Native APK (`ReelBlocker.apk`)
If you want an installed native Android app:

1. **[Direct Download `ReelBlocker.apk`](https://github.com/QuosaiAlqreini/ReelBlocker/releases/latest/download/ReelBlocker.apk)** (or check the [Releases Page](https://github.com/QuosaiAlqreini/ReelBlocker/releases)).
2. Open the downloaded `.apk` file on your phone and tap **Install** *(if prompted, allow "Install unknown apps" for your browser/file manager)*.
3. Launch **ReelBlocker** from your app drawer! Works on all modern Android devices, tablets, and foldables (Android 7.0+ up to Android 15).

---

### 🧩 Method 3: Mobile Browser Extension / Userscript
If you prefer using your existing mobile browser (Safari with Userscripts, Kiwi Browser, Orion, or Firefox Mobile):

1. Install **Tampermonkey** or the **Userscripts** app.
2. Load the script: **[`instagram-block-reels.user.js`](./instagram-block-reels.user.js)**.
3. All Instagram pages will have Reels stripped while keeping `/direct/inbox/` active.

---

## 💻 Desktop Setup (Chrome / Edge / Brave / Firefox)

### Option A: Install as Unpacked Extension
1. Clone or download this repository.
2. Open your browser and navigate to the extensions page:
   - **Chrome / Brave:** `chrome://extensions/`
   - **Edge:** `edge://extensions/`
3. Toggle on **Developer mode** (top right).
4. Click **Load unpacked** and select this repository folder.

### Option B: Tampermonkey
Install [Tampermonkey](https://www.tampermonkey.net/) and add [`instagram-block-reels.user.js`](./instagram-block-reels.user.js).

---

## ✨ What ReelBlocker Does

- 🚫 **Hides Reels Navigation Tab:** Strips the Reels icon completely from the navigation bar.
- 🔀 **Instant Redirect:** Any `/reels/*` or `/reel/*` link redirects straight to your Messages inbox (`/direct/inbox/`).
- 🧹 **Clean Feed:** Removes reels recommendation trays and video feeds from Home and Explore.
- 💬 **Full DM Support:** Retains full access to chats, messaging, photo sharing, and notifications.
- 🔋 **Zero Battery Drain:** Lightweight, fast, and privacy-respecting.

---

## 📁 Repository Structure

```text
ReelBlocker/
├── index.html                     # Mobile PWA installation portal
├── manifest.webmanifest           # Progressive Web App manifest
├── sw.js                          # Service Worker for offline & standalone install
├── manifest.json                  # Manifest V3 browser extension
├── content.js                     # URL interception & redirect logic
├── styles.css                     # CSS rules to hide Reels elements
├── instagram-block-reels.user.js  # Universal Userscript (Mobile & Desktop)
├── android/                       # Native Android WebView app project
│   └── app/src/main/
│       ├── AndroidManifest.xml
│       └── java/com/reelblocker/app/MainActivity.java
├── .github/workflows/
│   └── build-apk.yml              # Automated cloud builder for ReelBlocker.apk
├── icon16.png / icon48.png / icon128.png / icon192.png / icon512.png
└── README.md                      # Documentation
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
