# 🛡️ ReelBlocker

> **Block Instagram Reels, stay focused, and keep your Direct Messages (DMs) 100% accessible.**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Platform: Web / Mobile](https://img.shields.io/badge/Platform-Chrome%20%7C%20Safari%20%7C%20Firefox%20%7C%20Mobile-blue)](https://github.com/QuosaiAlqreini/ReelBlocker)

ReelBlocker removes the addictive Reels algorithm, bottom navigation tab, and explore video feeds from Instagram while ensuring your **Direct Messages (DMs)**, message notifications, and chat features remain completely available.

---

## ✨ Features

- 🚫 **Hides Reels Navigation Tab:** Completely eliminates the Reels icon from the bottom navigation bar and sidebar.
- 🔀 **Instant Redirection:** Visiting any `/reels/*` or `/reel/*` link instantly redirects you directly to your Messages inbox (`/direct/inbox/`).
- 🧹 **Feed Cleaner:** Suppresses Reels trays and recommendation algorithms from your home feed and explore page.
- 💬 **Full DM Support:** Retains unrestricted access to all your messages, chat threads, media sharing, and notifications.
- 📱 **Cross-Platform:** Works on **iOS (iPhone)**, **Android**, and **Desktop** (Chrome, Edge, Brave, Firefox, Safari).

---

## 📱 Mobile Setup Guide

### 🍎 iPhone (iOS)

#### Option 1: Direct Messages Home Screen App (Fastest, zero install)
1. Open **Safari** on your iPhone and visit: **[`https://www.instagram.com/direct/inbox/`](https://www.instagram.com/direct/inbox/)**
2. Log in to your account.
3. Tap the **Share** button (the square with an arrow pointing up at the bottom).
4. Tap **"Add to Home Screen"**.
5. Name it **"Instagram"** and tap **Add**.
6. **Result:** An icon appears on your home screen that opens in standalone fullscreen mode straight to your Messages inbox without any Reels tab.

#### Option 2: Safari Userscript Extension
1. Install **[Userscripts](https://apps.apple.com/app/userscripts/id1463298887)** (Free & Open Source) from the App Store.
2. Go to iPhone **Settings** → **Safari** → **Extensions** and enable **Userscripts**.
3. Open Safari, tap the extensions icon, and create a new script.
4. Paste the code from [`instagram-block-reels.user.js`](./instagram-block-reels.user.js).
5. **Result:** Reels are completely stripped from Instagram in Safari.

---

### 🤖 Android

#### Option 1: Instander / MyInsta (Native Android App)
1. Install **Instander** or **MyInsta** (popular Instagram clients for Android).
2. Go to **Profile** → hamburger menu (top right) → **Instander Settings** (or **MyInsta Settings**).
3. Select **Feed & Stories** and toggle on **"Hide Reels"** / **"Disable Reels button"**.
4. **Result:** The Reels button disappears from the bottom navigation bar completely.

#### Option 2: Kiwi Browser / Firefox Android + Extension / Userscript
1. Install **Kiwi Browser** or **Firefox** from the Google Play Store (both support extensions).
2. Install the **Tampermonkey** extension and load [`instagram-block-reels.user.js`](./instagram-block-reels.user.js), or load this folder as an unpacked extension.
3. Open `instagram.com`, tap the three dots in your browser, and tap **"Add to Home screen"** / **"Install App"**.

---

## 💻 Desktop Setup (Chrome / Edge / Brave / Firefox)

### Option A: Install as Unpacked Extension
1. Clone or download this repository.
2. Open your browser and navigate to the extensions page:
   - **Chrome / Brave:** `chrome://extensions/`
   - **Edge:** `edge://extensions/`
3. Toggle on **Developer mode** (top right).
4. Click **Load unpacked** and select this repository folder.

### Option B: Install via Tampermonkey / Violentmonkey
1. Install the [Tampermonkey](https://www.tampermonkey.net/) extension.
2. Click on the extension icon and select **Create a new script**.
3. Paste the contents of [`instagram-block-reels.user.js`](./instagram-block-reels.user.js) and save.

---

## 📁 Repository Structure

```text
ReelBlocker/
├── manifest.json                  # Manifest V3 extension configuration
├── content.js                     # URL redirection and DOM observer script
├── styles.css                     # CSS rules to hide Reels elements instantly
├── instagram-block-reels.user.js  # Universal Userscript (Mobile & Desktop)
├── icon16.png                     # Extension icon (16x16)
├── icon48.png                     # Extension icon (48x48)
├── icon128.png                    # Extension icon (128x128)
├── icon.png                       # Extension icon
└── README.md                      # Documentation
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
