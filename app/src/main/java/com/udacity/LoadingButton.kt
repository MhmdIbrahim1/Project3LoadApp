package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = ""
    // Variables to cache the attributed value
    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0
    private var buttonLoadingColor = 0
    private var circleColor = 0
    private var loadingStatus = 0.0f
    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, status ->
        when (status) {
            ButtonState.Loading -> {
                buttonText = resources.getString(R.string.button_loading)
                this.isClickable = false
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                buttonText = resources.getString(R.string.button_download)
                this.isClickable = true
                valueAnimator.cancel()
                progress = 0
            }
            ButtonState.Clicked -> {
                this.isClickable = false
            }
        }
        invalidate()
    }

    // Initialize paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 70.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    var progress = 0
    init {
        // Set button to be clickable
        isClickable = true
        // custom attributes
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_textColor, 0)
            buttonLoadingColor = getColor(R.styleable.LoadingButton_buttonLoadingColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }
        buttonState = ButtonState.Completed

        valueAnimator.apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // button background
        paint.color = buttonBackgroundColor
        canvas?.drawRect(0f,0f,widthSize.toFloat(), heightSize.toFloat(), paint)

        // paint loading
        paint.color = buttonLoadingColor
        canvas?.drawRect(((width/2-widthSize/2).toFloat()),
            ((height/2 - heightSize/2).toFloat()),
            (width/2-widthSize/2+loadingStatus), ((height/2+heightSize/2).toFloat()), paint)

        // paint the text
        paint.color = buttonTextColor
        canvas?.drawText(buttonText, widthSize/2.0f, heightSize/2.0f + 31.0f, paint)

        // circle
        paint.color = circleColor
        canvas?.drawArc(widthSize - 202f,50f,widthSize - 95f,143f,0f
            , progress.toFloat(), true, paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }


}