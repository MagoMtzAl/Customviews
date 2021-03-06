package com.mago.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R
import com.mago.customviews.util.Regex
import java.util.regex.Pattern

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */
open class CustomEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {
    // Attributes
    var onlyNumbers: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Paint objects
    private val framePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomEditText, 0, 0)
            .apply {
                try {
                    onlyNumbers =
                        getBoolean(R.styleable.CustomEditText_onlyNumbers, false)
                    charsWithBlankSpaces =
                        getBoolean(R.styleable.CustomEditText_charsWithBlankSpaces, false)
                    isMandatory = getBoolean(R.styleable.CustomEditText_isMandatory, false)


                    background = null
                } finally {
                    recycle()
                }
            }


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textIsEmpty = text.toString().isEmpty()

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            if (isMandatory)
                if (textIsEmpty)
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, frameAlertPaint)
                else
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)
            else
                drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)

        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(textWatcher())
        inputType = if (onlyNumbers)
            InputType.TYPE_CLASS_NUMBER
        else
            InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(textWatcher())
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.let { editable ->
                val count = editable.length

                // Pattern for only text
                val pattern = when {
                    onlyNumbers -> Regex.ONLY_NUMBERS
                    charsWithBlankSpaces -> Regex.A_TO_Z_WITH_BLANK_SPACES
                    else -> Regex.A_TO_Z
                }
                val mPattern = Pattern.compile(pattern)

                if (editable.toString() != "") {
                    val matcher = mPattern.matcher(editable[count - 1].toString())

                    if (!matcher.matches()) {
                        val text = s.subSequence(0, count - 1)
                        setText(text)
                        setSelection(count - 1)
                    }
                }
            }
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            invalidate()
        }
    }

}