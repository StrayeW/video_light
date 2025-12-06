package com.example.video_light // Kendi paket adınız

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import androidx.annotation.NonNull
import com.example.video_light.OverlayService

// Tüm metotlar bu sınıfın süslü parantezleri içinde olmalı!
class MainActivity: FlutterActivity() { 
    private val CHANNEL = "com.example.whatsapp_border_light/overlay"
    private val REQUEST_CODE_OVERLAY = 1234 

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->

            if (call.method == "startOverlay") {
                if (checkOverlayPermission()) {
                    val overlayIntent = Intent(this, OverlayService::class.java)
                    startService(overlayIntent)
                    result.success("Overlay Servisi Başlatıldı.")
                } else {
                    requestOverlayPermission()
                    result.error("PERMISSION_DENIED", "Overlay izni gerekli. Kullanıcı ayarlara yönlendirildi.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    // ✅ DOĞRU YER: Bu metotlar, MainActivity sınıfının içindedir.
    private fun checkOverlayPermission(): Boolean {
        // Android M (23) veya üzeri için kontrol
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)
    }

    // ✅ DOĞRU YER: Bu metotlar, MainActivity sınıfının içindedir.
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_CODE_OVERLAY)
        }
    }
} // <-- MainActivity sınıfının kapanışı