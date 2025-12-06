package com.example.video_light // Paket adının doğru olduğundan emin ol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class BorderLightView(context: Context, initialWidth: Float, initialColor: Int) : View(context) {

    private val lightPaint = Paint().apply {
        color = initialColor // Başlangıç rengi
        style = Paint.Style.STROKE
        strokeWidth = initialWidth // Başlangıç kalınlığı
        isAntiAlias = true
    }

    init {
        postDelayed(::updateAnimation, 50)
    }

    // 💡 Flutter'dan gelen yeni değerleri burada işliyoruz
    fun updateStyle(width: Float, color: Int) {
        lightPaint.strokeWidth = width
        lightPaint.color = color
        invalidate() // Görünümü hemen yenile
    }

    private fun updateAnimation() {
        invalidate()
        postDelayed(::updateAnimation, 50)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Çerçeveyi ekranın tam kenarlarına oturtmak için
        // Kalınlığın yarısı kadar içeri pay bırakmak daha düzgün görüntü sağlar
        val offset = lightPaint.strokeWidth / 2
        
        canvas.drawRect(
            offset, 
            offset, 
            width.toFloat() - offset, 
            height.toFloat() - offset, 
            lightPaint
        )
    }
}