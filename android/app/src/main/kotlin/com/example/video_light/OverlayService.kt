package com.example.video_light // Projenizin paket adı

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.WindowManager
import android.util.Log
import com.example.video_light.BorderLightView

class OverlayService : Service() {
    
    private lateinit var windowManager: WindowManager
    // View'ı nullable (?) ve kendi sınıfımız tipinde tanımlıyoruz ki fonksiyonlarına erişebilelim
    private var borderLightView: BorderLightView? = null 
    
    override fun onBind(intent: Intent?): IBinder? {
        return null 
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("OverlayService", "Servis Tetiklendi.")

        // 1. MainActivity'den gelen verileri al (Varsayılan değerlerle)
        val width = intent?.getFloatExtra("width", 10f) ?: 10f
        val color = intent?.getIntExtra("color", Color.WHITE) ?: Color.WHITE

        // WindowManager'ı başlat (Eğer başlatılmamışsa)
        if (!::windowManager.isInitialized) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        }

        // 2. View daha önce eklenmiş mi kontrol et
        if (borderLightView == null) {
            // --- İLK KEZ OLUŞTURMA ---
            Log.d("OverlayService", "Yeni View oluşturuluyor...")
            
            // BorderLightView sınıfına genişlik ve renk parametrelerini gönderiyoruz
            // (NOT: BorderLightView constructor'ını buna göre güncellemiş olman lazım)
            borderLightView = BorderLightView(this, width, color)

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_PHONE
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
            )

            windowManager.addView(borderLightView, params)
        } else {
            // --- GÜNCELLEME ---
            // View zaten ekranda, sadece rengini ve kalınlığını değiştir
            Log.d("OverlayService", "Mevcut View güncelleniyor: Kalınlık $width, Renk $color")
            borderLightView?.updateStyle(width, color)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OverlayService", "Servis Durduruldu.")
        
        if (borderLightView != null) {
            windowManager.removeView(borderLightView)
            borderLightView = null
        }
    }
}