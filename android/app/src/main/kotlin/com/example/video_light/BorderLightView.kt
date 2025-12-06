package com.example.video_light // Projenizin paket adı

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.util.Log
import android.view.View

class BorderLightView(context: Context) : View(context) {

    private val lightPaint = Paint().apply {
        color = Color.WHITE // Işık rengi
        style = Paint.Style.STROKE // Çizgi stili
        strokeWidth = 10f // Çizgi kalınlığı
        isAntiAlias = true
    }
    
    // Animasyon döngüsü için
    init {
        // Her 50 milisaniyede bir tekrar çizim yap (Animasyon etkisi için)
        postDelayed(::updateAnimation, 50)
    }

    private fun updateAnimation() {
        // Görünümün yeniden çizilmesini tetikler (onDraw metodunu çağırır)
        invalidate() 
        postDelayed(::updateAnimation, 50)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // 💡 Ekranın kenar çerçevesini çiz
        // Rect(left, top, right, bottom)
        canvas.drawRect(
            0f, 
            0f, 
            width.toFloat(), 
            height.toFloat(), 
            lightPaint
        )
        
        // WhatsApp görüntülü konuşma sırasında ekranın dört kenarında 
        // sürekli yanan beyaz şerit efekti elde edilmiş olur.
    }
}