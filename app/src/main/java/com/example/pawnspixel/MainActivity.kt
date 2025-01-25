package com.example.pawnspixel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        val slideDown = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_left)
        videoView = findViewById(R.id.videoView)

        val path = "android.resource://$packageName/${R.raw.splash}"
        val uri = Uri.parse(path)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mediaPlayer ->
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            val scaleX = screenWidth.toFloat() / videoWidth
            val scaleY = screenHeight.toFloat() / videoHeight
            val scale = scaleX.coerceAtLeast(scaleY)

            val layoutParams = videoView.layoutParams
            layoutParams.width = (videoWidth * scale).toInt()
            layoutParams.height = (videoHeight * scale).toInt()
            videoView.layoutParams = layoutParams

            videoView.start()
        }

        videoView.setOnCompletionListener {
            window.decorView.startAnimation(slideDown)
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}