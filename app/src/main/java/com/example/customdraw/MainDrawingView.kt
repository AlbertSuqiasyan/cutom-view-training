package com.example.customdraw

import android.animation.Animator
import android.view.View

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import java.lang.Float.min
import android.animation.ValueAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


class MainDrawingView
    (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val blendPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val bitmapPaint = Paint()
    var bitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    private var maskCanvas = Canvas()
    var maskBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var rectFBoundry: RectF

    var rotateDegree = 0f
    var angle = 90f
    var animator = ValueAnimator()

    var animatorIsRuning = false

    init {
//        bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        paint.isAntiAlias = true
        paint.strokeWidth = 40f
        paint.alpha = 50
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

        rectFBoundry = RectF()
        blendPaint.alpha = 50
        blendPaint.isFilterBitmap = true
        //blendPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        bitmap?.let {
            val sx: Float = width / it.width.toFloat()
            val sy: Float = height / it.height.toFloat()
            val s = min(sx, sy)

            canvas.save()
            canvas.scale(s, s)
            canvas.translate((width - it.width * s) / 2f / s, (height - it.height * s) / 2f / s)

            canvas.save()
            var cx = it.width / 2f
            var cy = it.height / 2f

            canvas.rotate(rotateDegree, cx, cy)

            canvas.drawBitmap(it, 0f, 0f, bitmapPaint)


            canvas.restore()

            canvas.clipRect(
                (width - it.width * s) / 2f,
                (height - it.height * s) / 2f,
                (width - it.width * s) / 2f + it.width * s,
                (height - it.height * s) / 2f + it.height * s
            )
            canvas.restore()

            canvas.drawBitmap(maskBitmap, 0f, 0f, blendPaint)
        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply {
                eraseColor(Color.GREEN)
                invalidate()
            }
            maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            maskCanvas = Canvas(maskBitmap)
        }

    }


    fun updateMask() {
//         maskCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

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

    fun savePathPorterDuffed() {
        blendPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        blendPaint.alpha = 255
        invalidate()
    }

    fun restorePorterDuffChanges() {
        blendPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        blendPaint.alpha = 50
        invalidate()
    }

    fun rotateRight() {

        if (!animator.isRunning) {

            animator = ValueAnimator.ofFloat(rotateDegree, rotateDegree + angle)
            animator.duration = 2000
            animator.interpolator = FastOutSlowInInterpolator()
            animator.addUpdateListener {
                rotateDegree = it.animatedValue as Float
                invalidate()
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })

            animator.start()
        }
    }

    fun rotateLeft() {
        if (!animator.isRunning) {

            animator = ValueAnimator.ofFloat(rotateDegree, rotateDegree - angle)
            animator.duration = 2000
            animator.interpolator = FastOutSlowInInterpolator()
            animator.addUpdateListener {
                rotateDegree = it.animatedValue as Float
                invalidate()
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })

            animator.start()
        }
    }
}