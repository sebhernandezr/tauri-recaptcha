package com.plugin.recaptcha

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.webkit.WebView
import app.tauri.annotation.Command
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@TauriPlugin
class RecaptchaPlugin(private val activity: Activity) : Plugin(activity) {

    private var webView: WebView? = null // Keep a reference to the WebView

    @SuppressLint("SetJavaScriptEnabled")
    @Command
    fun doRecaptchaChallenge(invoke: Invoke) {
        // Get screen dimensions
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        // Create a WebView instance
        webView = WebView(activity)
        webView?.apply {
            setBackgroundColor(Color.TRANSPARENT)

            // Set WebView size to half the screen height
            val layoutParams = FrameLayout.LayoutParams(
                screenWidth / 2,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.gravity = Gravity.BOTTOM // Position at the bottom of the screen

            // Add WebView to the activity
            activity.addContentView(this, layoutParams)

            // Enable JavaScript for the WebView
            settings.javaScriptEnabled = true

            // Load google.com in the WebView
            loadUrl("https://www.geogebra.org/classic")
        }
    }

    @Command
    fun closeRecaptchaChallenge(invoke: Invoke) {
        webView?.let { webView ->
            // Remove the WebView from its parent
            (webView.parent as? ViewGroup)?.removeView(webView)
            // Destroy the WebView
            webView.destroy()
            // Nullify the reference
            this.webView = null
        }
    }
}
