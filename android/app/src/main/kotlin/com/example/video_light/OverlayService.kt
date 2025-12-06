package com.example.video_light // Projenizin paket adı

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.util.Log

// !!! BU SATIRI EKLEYİNİZ !!!
import com.example.video_light.BorderLightView 


class OverlayService : Service() {
    
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    
    override fun onBind(intent: Intent?): IBinder? {
        return null // Bağlı servis gerekmiyor
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("OverlayService", "Servis Başlatıldı. Pencere Yöneticisine Ekleniyor...")
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // 1. Kenar Işığı View'ını oluştur
        // Burada artık BorderLightView sınıfını tanıyacak
        overlayView = BorderLightView(this) 
        
        // 2. Pencere Parametrelerini Ayarla
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
        
        // 3. View'ı ekrana ekle
        windowManager.addView(overlayView, params)

        return START_STICKY 
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OverlayService", "Servis Sonlandırıldı. Pencere Kaldırılıyor.")
        
        if (::windowManager.isInitialized && ::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }
}