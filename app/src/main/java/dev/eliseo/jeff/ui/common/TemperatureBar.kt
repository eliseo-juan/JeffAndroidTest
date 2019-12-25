package dev.eliseo.jeff.ui.common

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import dev.eliseo.jeff.R
import kotlin.math.roundToInt


class TemperatureBar : FrameLayout {

    private var barImageView: ImageView? = null

    private val temperature = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_temperature_bar, this, true) as FrameLayout
        barImageView = view.findViewById(R.id.imageViewBar)

    }

    private fun loadAnimation(fromTemperature: Int, toTemperature: Int) {
        val widthAnimator = ValueAnimator.ofInt(getWidth(fromTemperature), getWidth(toTemperature))
        widthAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            this.barImageView?.layoutParams?.width = animatedValue
            this.barImageView?.requestLayout()
        }

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), getColor(temperature), getColor(toTemperature))
        colorAnimation.addUpdateListener { animator ->
            this.barImageView?.imageTintList = ColorStateList.valueOf(animator.animatedValue as Int)
        }

        AnimatorSet().apply {
            playTogether(widthAnimator, colorAnimation)
            duration = 1000
            start()
        }
        barImageView?.visibility = View.VISIBLE
    }

    fun setTemperature(temperature: Int) {
        loadAnimation(this.temperature, temperature)
    }

    private fun getMaxValue(): Int {
        return this.width
    }
    private fun getWidth(temperature: Int): Int {
        val fraction: Float = ((temperature.toFloat() - MIN_TEMP.toFloat()) / (MAX_TEMP.toFloat() - MIN_TEMP.toFloat()))
        return (fraction * getMaxValue()).roundToInt()
    }

    @ColorInt
    private fun getColor(temperature: Int) : Int {
        return when {
            temperature >= 30 -> ContextCompat.getColor(context, R.color.tempHigh)
            temperature >= 16 -> ContextCompat.getColor(context, R.color.tempMedium)
            else -> ContextCompat.getColor(context, R.color.tempLow)
        }
    }

    companion object {
        private const val MIN_TEMP = 0
        private const val MAX_TEMP = 40
    }
}