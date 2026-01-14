package com.apptive.japkor

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.apptive.japkor.widget.FullscreenVideoView

class SplashActivity : ComponentActivity() {
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val videoView = findViewById<FullscreenVideoView>(R.id.splashVideo)
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.n_splash}")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            videoView.updateVideoSize(mediaPlayer.videoWidth, mediaPlayer.videoHeight)
            mediaPlayer.isLooping = false
            mediaPlayer.setVideoScalingMode(
                MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            )
            videoView.start()
        }
        videoView.setOnCompletionListener {
            videoView.stopPlayback()
            navigateToMain()
        }
        videoView.setOnErrorListener { _, _, _ ->
            navigateToMain()
            true
        }
    }

    private fun navigateToMain() {
        if (hasNavigated) return
        hasNavigated = true
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(this.intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}
