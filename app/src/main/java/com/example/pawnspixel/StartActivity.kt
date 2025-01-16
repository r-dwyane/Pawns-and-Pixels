package com.example.pawnspixel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.droidsonroids.gif.GifImageButton
import pl.droidsonroids.gif.GifImageView

class StartActivity : AppCompatActivity() {
    private lateinit var gifImageView: GifImageButton
    private lateinit var startButton: Button
    private var tapCount = 0
    private var lastTapTime: Long = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gifImageView = findViewById(R.id.gif)
        startButton = findViewById(R.id.button)

        gifImageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - lastTapTime < 1000) { // Within 1 second
                    tapCount++
                } else {
                    tapCount = 1
                }
                lastTapTime = currentTime

                if (tapCount >= 5) {
                    val fragment = AdminSignInFragment()
                    fragment.show(supportFragmentManager, "Android Center")
                    tapCount = 0
                }
            }
            true
        }

//        val sharedPrefManager = SharedPrefManager(this)
//        if (sharedPrefManager.getUserEmail() != null && sharedPrefManager.getUserName() != null && sharedPrefManager.getUserId() != null) {
//
//            SessionManager.email = sharedPrefManager.getUserEmail()
//            SessionManager.name = sharedPrefManager.getUserName()
//            SessionManager.userId = sharedPrefManager.getUserId()
//
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        startButton.setOnClickListener {
            val fragment2 = CustomerSignUpFragment()
            fragment2.show(supportFragmentManager, "Android Center")
        }
    }
}