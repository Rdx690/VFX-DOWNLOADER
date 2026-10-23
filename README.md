# VFX DOWNLOADER — SOURAJIT VFX

Android downloader starter app using Kotlin and WebView.

## Build APK
1. Open the Actions tab.
2. Select **Build VFX Downloader APK** and tap **Run workflow**.
3. Open the completed run and download **VFX-DOWNLOADER-APK**.
4. Extract app-debug.apk from the artifact ZIP.

API: https://api.sayan-nexuswork.workers.dev/alldown?url=<URL-encoded-source-link>

The app detects common direct media URL fields. API response formats can change. MP3 requires an audio URL from the API; the app does not convert video to audio. HLS streams are not merged to MP4. Download only content you have permission to save.
