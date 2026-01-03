package com.apptive.japkor.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class FullscreenVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {
    private var videoWidth = 0
    private var videoHeight = 0

    fun updateVideoSize(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        videoWidth = width
        videoHeight = height
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (videoWidth == 0 || videoHeight == 0 || viewWidth == 0 || viewHeight == 0) {
            setMeasuredDimension(viewWidth, viewHeight)
            return
        }

        val viewRatio = viewWidth.toFloat() / viewHeight
        val videoRatio = videoWidth.toFloat() / videoHeight

        if (videoRatio > viewRatio) {
            val scaledWidth = (viewHeight * videoRatio).toInt()
            setMeasuredDimension(scaledWidth, viewHeight)
        } else {
            val scaledHeight = (viewWidth / videoRatio).toInt()
            setMeasuredDimension(viewWidth, scaledHeight)
        }
    }
}
