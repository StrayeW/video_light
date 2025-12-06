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

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.whatsapp_border_light/overlay"
    private val REQUEST_CODE_OVERLAY = 1234

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->

            if (call.method == "startOverlay") {
                // 1. Flutter'dan gelen verileri (argümanları) alıyoruz
                // Eğer veri gelmezse varsayılan değerler (?:) kullanılır.
                val width = call.argument<Double>("width")?.toFloat() ?: 10f
                val colorLong = call.argument<Long>("color") ?: 0xFFFFFFFF
                val color = colorLong.toInt() // Flutter renkleri Long gönderir, biz Int'e çeviriyoruz

                if (checkOverlayPermission()) {
                    val overlayIntent = Intent(this, OverlayService::class.java)
                    
                    // 2. Bu verileri Servis'e taşıyoruz (putExtra ile)
                    overlayIntent.putExtra("width", width)
                    overlayIntent.putExtra("color", color)

                    // Servisi başlat
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(overlayIntent)
                    } else {
                        startService(overlayIntent)
                    }
                    
                    result.success("Overlay Servisi Başlatıldı. Genişlik: $width, Renk: $color")
                } else {
                    requestOverlayPermission()
                    result.error("PERMISSION_DENIED", "Overlay izni gerekli. Kullanıcı ayarlara yönlendirildi.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    private fun checkOverlayPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_CODE_OVERLAY)
        }
    }
}