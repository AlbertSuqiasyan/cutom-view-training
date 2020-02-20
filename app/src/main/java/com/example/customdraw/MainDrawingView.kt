package com.example.customdraw

import android.view.View

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import java.lang.Float.min
import kotlin.math.max

class MainDrawingView
    (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val blendPaint = Paint()
    private val path = Path()
    private val bitmapPaint = Paint()
    var bitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    var chosenBitmap: Bitmap? = null

    private var isChangedBitmap = false
    private var maskCanvas = Canvas()
    var maskBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    init {
        bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        paint.isAntiAlias = true
        paint.strokeWidth = 40f
        paint.alpha = 50
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)


        blendPaint.isAntiAlias = true
        blendPaint.isFilterBitmap = true
        //blendPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        bitmap?.let {
            val sx:Float = width/it.width.toFloat()
            val sy:Float = height/it.height.toFloat()
            val s = min(sx,sy)
            val middleOfx: Float = width/2.toFloat()
            val middleOfy: Float = height/2.toFloat()
            val middleOfBitmapX: Float = it.width/2.toFloat()
            val middleOfBitmapY: Float = it.height/2.toFloat()

            canvas.save()
            canvas.scale(s,s)
            canvas.translate((width - it.width*s)/2f/s,(height - it.height*s)/2f/s)
//            if (middleOfx > middleOfBitmapX){
//                canvas.translate(middleOfx,0f)
//            }
//            if (middleOfx < middleOfBitmapX){
//
//            }
//            if (middleOfy>middleOfBitmapY){
//                canvas.translate(0f,middleOfy)
//            }
            canvas.drawBitmap(it, 0f, 0f, bitmapPaint)
            canvas.restore()
        }
        canvas.drawBitmap(maskBitmap, 0f, 0f, blendPaint)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && !isChangedBitmap) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply {
                eraseColor(Color.GREEN)
                invalidate()
            }
            maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            maskCanvas = Canvas(maskBitmap)
        }

        if (w > 0 && isChangedBitmap) {
            chosenBitmap = bitmap?.let { Bitmap.createScaledBitmap(it, w, h, true) }
            invalidate()
        }
    }


    fun updateMask() {
        // maskCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        maskCanvas.drawPath(path, paint)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var eventX = event.x
        var eventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(eventX, eventY)
                return true
            }
            MotionEvent.ACTION_MOVE ->
                path.lineTo(eventX, eventY)
            else -> return false
        }

        updateMask()
        return true
    }

    fun changeBitmap() {

        isChangedBitmap = true
    }

}