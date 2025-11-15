package com.apptive.japkor.ui.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.apptive.japkor.R
import kotlin.math.roundToInt

/**
 * App-wide toast helper that provides a consistent look & feel.
 */
object CustomToast {

    private const val TAG = "CustomToast"

    enum class ToastType {
        DEFAULT,
        SUCCESS,
        ERROR,
        DEBUG
    }

    private data class ToastStyle(
        val backgroundColor: Int,
        val borderColor: Int,
        val textColor: Int
    )

    private var currentToast: Toast? = null

    fun show(
        context: Context,
        message: String,
        type: ToastType = ToastType.DEFAULT,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        logMessage(type, message)

        val appContext = context.applicationContext
        val toastView = LayoutInflater.from(appContext)
            .inflate(R.layout.view_custom_toast, null)

        val messageView = toastView.findViewById<TextView>(R.id.toast_message)
        messageView.text = message

        applyStyle(appContext, toastView, messageView, type)

        ViewCompat.setElevation(toastView, 12f)

        currentToast?.cancel()
        currentToast = Toast(appContext).apply {
            view = toastView
            this.duration = duration
            setGravity(
                Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                0,
                appContext.resources.getDimensionPixelSize(R.dimen.toast_vertical_offset)
            )
        }

        currentToast?.show()
    }

    fun show(
        context: Context,
        @StringRes messageRes: Int,
        type: ToastType = ToastType.DEFAULT,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        show(context, context.getString(messageRes), type, duration)
    }

    fun showLong(context: Context, message: String, type: ToastType = ToastType.DEFAULT) {
        show(context, message, type, Toast.LENGTH_LONG)
    }

    fun showLong(
        context: Context,
        @StringRes messageRes: Int,
        type: ToastType = ToastType.DEFAULT
    ) {
        show(context, context.getString(messageRes), type, Toast.LENGTH_LONG)
    }

    fun showSuccess(context: Context, message: String, long: Boolean = false) {
        if (long) {
            showLong(context, message, ToastType.SUCCESS)
        } else {
            show(context, message, ToastType.SUCCESS)
        }
    }

    fun showError(context: Context, message: String, long: Boolean = false) {
        if (long) {
            showLong(context, message, ToastType.ERROR)
        } else {
            show(context, message, ToastType.ERROR)
        }
    }

    fun showDebug(context: Context, message: String, long: Boolean = false) {
        if (long) {
            showLong(context, message, ToastType.DEBUG)
        } else {
            show(context, message, ToastType.DEBUG)
        }
    }

    private fun applyStyle(
        context: Context,
        root: View,
        messageView: TextView,
        type: ToastType
    ) {
        val style = resolveStyle(context, type)
        messageView.setTextColor(style.textColor)

        val background = root.background
        if (background is GradientDrawable) {
            background.mutate()
            background.setColor(style.backgroundColor)
            background.setStroke(root.dpToPx(1f), style.borderColor)
        } else {
            root.setBackgroundColor(style.backgroundColor)
        }
    }

    private fun resolveStyle(context: Context, type: ToastType): ToastStyle {
        return when (type) {
            ToastType.DEFAULT -> ToastStyle(
                backgroundColor = ContextCompat.getColor(context, R.color.toast_background),
                borderColor = ContextCompat.getColor(context, R.color.toast_border),
                textColor = ContextCompat.getColor(context, R.color.toast_text)
            )

            ToastType.SUCCESS -> ToastStyle(
                backgroundColor = ContextCompat.getColor(context, R.color.toast_background),
                borderColor = ContextCompat.getColor(context, R.color.toast_success_border),
                textColor = ContextCompat.getColor(context, R.color.toast_text)
            )

            ToastType.ERROR -> ToastStyle(
                backgroundColor = ContextCompat.getColor(context, R.color.toast_background),
                borderColor = ContextCompat.getColor(context, R.color.toast_error_border),
                textColor = ContextCompat.getColor(context, R.color.toast_text)
            )

            ToastType.DEBUG -> ToastStyle(
                backgroundColor = ContextCompat.getColor(context, R.color.toast_background),
                borderColor = ContextCompat.getColor(context, R.color.toast_debug_border),
                textColor = ContextCompat.getColor(context, R.color.toast_text)
            )
        }
    }

    private fun View.dpToPx(value: Float): Int {
        val density = resources.displayMetrics.density
        return (value * density).roundToInt().coerceAtLeast(1)
    }

    private fun logMessage(type: ToastType, message: String) {
        if (type == ToastType.ERROR) {
            Log.e(TAG, message)
        } else if (type == ToastType.DEBUG) {
            Log.d(TAG, message)
        } else {
            Log.i(TAG, message)
        }
    }
}
