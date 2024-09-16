package com.example.ringtuneandwallpaper.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class DetailIconView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val circlePaint = Paint().apply {
        color = Color.WHITE  // Màu của hình tròn
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.parseColor("#5871BA")  // Màu của chữ "i"
        textSize = 38f      // Kích thước chữ
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Vẽ hình tròn
        val padding = paddingStart.coerceAtMost(paddingEnd).coerceAtMost(paddingTop).coerceAtMost(paddingBottom)
        val radius = (width / 2f - padding).coerceAtMost(height / 2f - padding)
        val centerX = width / 2f
        val centerY = height / 2f
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Vẽ chữ "i"
        val textX = centerX
        val textY = centerY - (textPaint.descent() + textPaint.ascent()) / 2  // Căn giữa chữ
        canvas.drawText("i", textX, textY, textPaint)

    }
}