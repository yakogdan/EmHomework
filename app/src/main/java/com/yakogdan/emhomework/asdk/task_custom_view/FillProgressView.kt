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
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.withClip
import com.yakogdan.emhomework.R
import kotlin.math.max
import kotlin.random.Random

class FillProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var progressPercent: Int = 0

    private var stepPercent: Int = DEFAULT_STEP_PERCENT
    private var cornerRadiusPx: Float = DEFAULT_CORNER_RADIUS_DP.dpToPx()
    private var borderWidthPx: Float = DEFAULT_BORDER_WIDTH_DP.dpToPx()

    @ColorInt
    private var borderColor: Int = DEFAULT_BORDER_COLOR

    @ColorInt
    private var fillColor: Int = randomColor()

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val contentRect = RectF()

    init {
        isClickable = true

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                /* set = */ attrs,
                /* attrs = */ R.styleable.FillProgressView,
                /* defStyleAttr = */ defStyleAttr,
                /* defStyleRes = */ 0
            )
            try {
                stepPercent = typedArray.getInt(
                    /* index = */ R.styleable.FillProgressView_stepPercent,
                    /* defValue = */ DEFAULT_STEP_PERCENT
                )
                borderColor = typedArray.getColor(
                    /* index = */ R.styleable.FillProgressView_borderColor,
                    /* defValue = */ DEFAULT_BORDER_COLOR
                )
                borderWidthPx = typedArray.getDimension(
                    /* index = */ R.styleable.FillProgressView_borderWidth,
                    /* defValue = */ DEFAULT_BORDER_WIDTH_DP.dpToPx()
                )
                cornerRadiusPx = typedArray.getDimension(
                    /* index = */ R.styleable.FillProgressView_cornerRadius,
                    /* defValue = */ DEFAULT_CORNER_RADIUS_DP.dpToPx()
                )
                progressPercent = typedArray
                    .getInt(R.styleable.FillProgressView_initialProgress, 0)
                    .coerceIn(0, 100)

            } finally {
                typedArray.recycle()
            }
        }

        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidthPx
        fillPaint.color = fillColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 250f.dpToPx().toInt()
        val desiredHeight = 50f.dpToPx().toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(
            /* measuredWidth = */ width,
            /* measuredHeight = */ height,
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val left = paddingLeft.toFloat() + borderWidthPx / 2f
        val right = (w - paddingRight).toFloat() - borderWidthPx / 2f
        val top = paddingTop.toFloat() + borderWidthPx / 2f
        val bottom = (h - paddingBottom).toFloat() - borderWidthPx / 2f

        contentRect.set(
            /* left = */ left,
            /* top = */ top,
            /* right = */ max(left, right),
            /* bottom = */ max(top, bottom)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val percent = progressPercent

        if (percent > 0) {
            val clipRight = contentRect.left + contentRect.width() * (percent / 100f)

            canvas.withClip(
                left = contentRect.left,
                top = contentRect.top,
                right = clipRight,
                bottom = contentRect.bottom,
            ) {
                drawRoundRect(
                    /* rect = */ contentRect,
                    /* rx = */ cornerRadiusPx,
                    /* ry = */ cornerRadiusPx,
                    /* paint = */ fillPaint,
                )
            }
        }

        canvas.drawRoundRect(
            /* rect = */ contentRect,
            /* rx = */ cornerRadiusPx,
            /* ry = */ cornerRadiusPx,
            /* paint = */ borderPaint,
        )
    }

    override fun performClick(): Boolean {
        setNewProgressAndRandomColor()
        return super.performClick()
    }

    private fun setNewProgressAndRandomColor() {
        val newValue = progressPercent + stepPercent
        progressPercent = if (newValue > 100) 0 else newValue

        fillColor = randomColor()
        fillPaint.color = fillColor

        invalidate()
    }

    fun setProgressPercent(value: Int) {
        val newValue = value.coerceIn(0, 100)
        if (newValue != progressPercent) {
            progressPercent = newValue
            invalidate()
        }
    }

    fun getProgressPercent(): Int = progressPercent

    fun reset() {
        if (progressPercent != 0) {
            progressPercent = 0
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

    fun setBorderWidthDp(width: Float) {
        val newWidth = max(0f, width.dpToPx())
        if (borderWidthPx != newWidth) {
            borderWidthPx = newWidth
            borderPaint.strokeWidth = newWidth
            requestLayout()
            invalidate()
        }
    }

    fun setCornerRadiusDp(radius: Float) {
        val newRadius = max(0f, radius.dpToPx())
        if (cornerRadiusPx != newRadius) {
            cornerRadiusPx = newRadius
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.progressPercent = progressPercent
        savedState.stepPercent = stepPercent
        savedState.cornerRadiusPx = cornerRadiusPx
        savedState.borderWidthPx = borderWidthPx
        savedState.borderColor = borderColor
        savedState.fillColor = fillColor
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            progressPercent = state.progressPercent.coerceIn(0, 100)
            stepPercent = state.stepPercent
            cornerRadiusPx = state.cornerRadiusPx
            borderWidthPx = state.borderWidthPx
            borderColor = state.borderColor
            fillColor = state.fillColor

            fillPaint.color = fillColor
            borderPaint.color = borderColor
            borderPaint.strokeWidth = borderWidthPx

            requestLayout()
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private inner class SavedState(superState: Parcelable?) : BaseSavedState(superState) {
        var progressPercent: Int = 0
        var stepPercent: Int = DEFAULT_STEP_PERCENT
        var cornerRadiusPx: Float = DEFAULT_CORNER_RADIUS_DP.dpToPx()
        var borderWidthPx: Float = DEFAULT_BORDER_WIDTH_DP.dpToPx()
        var borderColor: Int = DEFAULT_BORDER_COLOR
        var fillColor: Int = randomColor()

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(progressPercent)
            out.writeInt(stepPercent)
            out.writeFloat(cornerRadiusPx)
            out.writeFloat(borderWidthPx)
            out.writeInt(borderColor)
            out.writeInt(fillColor)
        }
    }

    private fun Float.dpToPx(): Float =
        TypedValue.applyDimension(
            /* unit = */ TypedValue.COMPLEX_UNIT_DIP,
            /* value = */ this,
            /* metrics = */ resources.displayMetrics,
        )

    @ColorInt
    private fun randomColor(): Int {
        val r = Random.nextInt(0, 256)
        val g = Random.nextInt(0, 256)
        val b = Random.nextInt(0, 256)
        return Color.rgb(r, g, b)
    }

    companion object {
        private const val DEFAULT_STEP_PERCENT = 10
        private const val DEFAULT_CORNER_RADIUS_DP = 10f
        private const val DEFAULT_BORDER_WIDTH_DP = 2f
        private const val DEFAULT_BORDER_COLOR = Color.DKGRAY
    }
}