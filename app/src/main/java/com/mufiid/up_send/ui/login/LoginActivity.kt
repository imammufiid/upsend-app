package com.mufiid.up_send.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mufiid.up_send.R
import com.mufiid.up_send.databinding.ActivityLoginBinding
import com.mufiid.up_send.ui.home.HomeActivity
import com.mufiid.up_send.ui.registration.RegistrationActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        initSupportActionBar()
        init()
    }

    private fun init() {
        activityLoginBinding.btnLogin.setOnClickListener(this)
        activityLoginBinding.btnRegistration.setOnClickListener(this)
    }

    private fun initSupportActionBar() {
        supportActionBar?.hide()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_login -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.btn_registration -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }
    }
}