package com.example.trendbazaar.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieDrawable
import com.example.trendbazaar.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.repeatCount = LottieDrawable.INFINITE

        val splashDuration: Long = 4000

        binding.animationView.postDelayed({
            val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, splashDuration)
    }
}