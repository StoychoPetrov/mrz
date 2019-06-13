package com.example.mrzreaderlibrary

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.graphics.*
import androidx.core.content.ContextCompat


class OverlayView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var bitmap: Bitmap? = null

    var bottomView      = 0f
    var topView         = 0f

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        if (bitmap == null)
            createWindowFrame();

        canvas?.drawBitmap(bitmap, 0f, 0f, null);
    }

    private fun createWindowFrame() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val osCanvas = Canvas(bitmap)

        val outerRectangle = RectF(0f, 0f, width.toFloat(), height.toFloat())

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = ContextCompat.getColor(context, R.color.camera_background)
        osCanvas.drawRect(outerRectangle, paint)

        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        val padding     = 11 * resources.displayMetrics.density

        val width       = resources.displayMetrics.widthPixels - 2 * padding

        val height      = width / 1.37f

        val radius      = 14 * resources.displayMetrics.density

        topView         = (resources.displayMetrics.heightPixels - height) / 2

        bottomView      = osCanvas.height - topView

        osCanvas.drawRoundRect(RectF(padding, topView, osCanvas.width - padding, bottomView), radius, radius, paint)
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        bitmap = null
    }
}