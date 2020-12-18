package com.mufiid.up_send.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mufiid.up_send.R
import com.mufiid.up_send.ui.login.LoginActivity
import com.mufiid.up_send.ui.onBoarding.OnBoardingActivity
import com.mufiid.up_send.utils.pref.OnBoardingPref

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            if(OnBoardingPref.getFirstLaunchApp(this)) {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}