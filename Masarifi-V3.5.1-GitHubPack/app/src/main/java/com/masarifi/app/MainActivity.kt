package com.masarifi.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : android.app.Activity() {
    private lateinit var webView: WebView
    private val notificationChannelId = "masarifi_alerts"
    private val notificationRequestCode = 351

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        webView = WebView(this)
        setContentView(webView)
        configureWebView()
        webView.addJavascriptInterface(MasarifiAndroidBridge(), "MasarifiAndroid")
        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun configureWebView() {
        webView.webViewClient = WebViewClient()
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = true
            builtInZoomControls = false
            displayZoomControls = false
        }
        WebView.setWebContentsDebuggingEnabled(false)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "تنبيهات مصاريفي",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "تنبيهات الميزانية والعمليات في مصاريفي"
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermissionFromWeb() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            notifyJsPermissionResult(true)
            return
        }
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notifyJsPermissionResult(true)
            return
        }
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), notificationRequestCode)
    }

    private fun notifyJsPermissionResult(granted: Boolean) {
        val value = if (granted) "granted" else "denied"
        webView.post {
            webView.evaluateJavascript("window.onNativeNotificationPermissionResult && window.onNativeNotificationPermissionResult('$value');", null)
        }
    }

    private fun showNativeNotification(title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "اسمح بإشعارات مصاريفي من إعدادات الهاتف أولاً", Toast.LENGTH_SHORT).show()
            return
        }

        val manager = getSystemService(NotificationManager::class.java)
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.Notification.Builder(this, notificationChannelId)
        } else {
            android.app.Notification.Builder(this)
        }

        val notification = builder
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title.ifBlank { "مصاريفي" })
            .setContentText(body)
            .setStyle(android.app.Notification.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .build()

        manager.notify((System.currentTimeMillis() and 0x7fffffff).toInt(), notification)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationRequestCode) {
            notifyJsPermissionResult(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onBackPressed() {
        if (!::webView.isInitialized) {
            super.onBackPressed()
            return
        }
        webView.evaluateJavascript(
            "(typeof closeTopModalFromBack_==='function' ? closeTopModalFromBack_() : false)",
        ) { result ->
            if (result != "true") super.onBackPressed()
        }
    }

    inner class MasarifiAndroidBridge {
        @JavascriptInterface
        fun requestNotificationPermission() {
            runOnUiThread { requestNotificationPermissionFromWeb() }
        }

        @JavascriptInterface
        fun showNotification(title: String, body: String) {
            runOnUiThread { showNativeNotification(title, body) }
        }
    }
}
