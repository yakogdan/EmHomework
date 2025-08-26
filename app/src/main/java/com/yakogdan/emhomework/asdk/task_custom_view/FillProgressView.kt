package com.yakogdan.emhomework.asdk.task_custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.yakogdan.emhomework.R
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class FillProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var stepPercent: Int = 10
    private var cornerRadiusPx: Float = 8f.dpToPx()
    private var borderWidthPx: Float = 2f.dpToPx()

    @ColorInt
    private var borderColor: Int = Color.DKGRAY

    private var progressPercent: Int = 0

    @ColorInt
    private var fillColor: Int = randomColor()

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val contentRect = RectF()

    private var isTouchDown = false

    init {
        isClickable = true

        if (attrs != null) {
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.FillProgressView, defStyleAttr, 0)
            try {
                stepPercent = a.getInt(R.styleable.FillProgressView_stepPercent, 10)
                borderColor = a.getColor(R.styleable.FillProgressView_borderColor, Color.DKGRAY)
                borderWidthPx =
                    a.getDimension(R.styleable.FillProgressView_borderWidth, 2f.dpToPx())
                cornerRadiusPx = a.getDimension(
                    R.styleable.FillProgressView_cornerRadius,
                    8f.dpToPx()
                )
                progressPercent = a
                    .getInt(R.styleable.FillProgressView_initialProgress, 0)
                    .coerceIn(0, 100)

            } finally {
                a.recycle()
            }
        }

        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidthPx
        fillPaint.color = fillColor

        updateContentDescription()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredW = 200f.dpToPx().toInt()
        val desiredH = 32f.dpToPx().toInt()

        val width = resolveSize(desiredW, widthMeasureSpec)
        val height = resolveSize(desiredH, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val left = paddingLeft.toFloat() + borderWidthPx / 2f
        val top = paddingTop.toFloat() + borderWidthPx / 2f
        val right = (w - paddingRight).toFloat() - borderWidthPx / 2f
        val bottom = (h - paddingBottom).toFloat() - borderWidthPx / 2f
        contentRect.set(left, top, max(left, right), max(top, bottom))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val pct = progressPercent
        if (pct > 0) {
            val clipRight = contentRect.left + contentRect.width() * (pct / 100f)
            val save = canvas.save()
            canvas.clipRect(contentRect.left, contentRect.top, clipRight, contentRect.bottom)
            canvas.drawRoundRect(contentRect, cornerRadiusPx, cornerRadiusPx, fillPaint)
            canvas.restoreToCount(save)
        }

        canvas.drawRoundRect(contentRect, cornerRadiusPx, cornerRadiusPx, borderPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isTouchDown = true
                parent?.requestDisallowInterceptTouchEvent(true)
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (isTouchDown && isWithinView(event.x, event.y)) {
                    isTouchDown = false
                    performClick()
                    return true
                }
                isTouchDown = false
            }

            MotionEvent.ACTION_CANCEL -> {
                isTouchDown = false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        advanceProgressAndColor()
        return super.performClick()
    }

    private fun isWithinView(x: Float, y: Float): Boolean {
        return x >= 0f && x <= width && y >= 0f && y <= height
    }

    private fun advanceProgressAndColor() {
        val newVal = progressPercent + stepPercent
        progressPercent = if (newVal > 100) 0 else min(100, newVal)

        fillColor = randomColor()
        fillPaint.color = fillColor

        updateContentDescription()
        invalidate()
    }

    private fun updateContentDescription() {
        contentDescription = "${progressPercent}%"
    }

    fun setProgressPercent(value: Int) {
        val clamped = value.coerceIn(0, 100)
        if (clamped != progressPercent) {
            progressPercent = clamped
            updateContentDescription()
            invalidate()
        }
    }

    fun getProgressPercent(): Int = progressPercent

    fun reset() {
        if (progressPercent != 0) {
            progressPercent = 0
            updateContentDescription()
            invalidate()
        }
    }

    fun setStepPercent(step: Int) {
        stepPercent = when {
            step <= 0 -> 1
            step > 100 -> 100
            else -> step
        }
    }

    fun setBorderColor(@ColorInt color: Int) {
        if (borderColor != color) {
            borderColor = color
            borderPaint.color = color
            invalidate()
        }
    }

    fun setBorderWidthPx(widthPx: Float) {
        val newW = max(0f, widthPx)
        if (borderWidthPx != newW) {
            borderWidthPx = newW
            borderPaint.strokeWidth = newW
            requestLayout()
            invalidate()
        }
    }

    fun setCornerRadiusPx(radiusPx: Float) {
        val r = max(0f, radiusPx)
        if (cornerRadiusPx != r) {
            cornerRadiusPx = r
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.progress = progressPercent
        ss.step = stepPercent
        ss.fillColor = fillColor
        ss.borderColor = borderColor
        ss.borderWidthPx = borderWidthPx
        ss.cornerRadiusPx = cornerRadiusPx
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            progressPercent = state.progress.coerceIn(0, 100)
            stepPercent = state.step
            fillColor = state.fillColor
            borderColor = state.borderColor
            borderWidthPx = state.borderWidthPx
            cornerRadiusPx = state.cornerRadiusPx

            fillPaint.color = fillColor
            borderPaint.color = borderColor
            borderPaint.strokeWidth = borderWidthPx

            updateContentDescription()
            requestLayout()
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var progress: Int = 0
        var step: Int = 10
        var fillColor: Int = Color.GREEN
        var borderColor: Int = Color.DKGRAY
        var borderWidthPx: Float = 0f
        var cornerRadiusPx: Float = 0f

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(progress)
            out.writeInt(step)
            out.writeInt(fillColor)
            out.writeInt(borderColor)
            out.writeFloat(borderWidthPx)
            out.writeFloat(cornerRadiusPx)
        }
    }

    private fun Float.dpToPx(): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    @ColorInt
    private fun randomColor(): Int {
        val r = Random.nextInt(0, 256)
        val g = Random.nextInt(0, 256)
        val b = Random.nextInt(0, 256)
        return Color.rgb(r, g, b)
    }
}