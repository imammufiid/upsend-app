package com.mufiid.up_send.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.up_send.R
import com.mufiid.up_send.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var activityRegistrationBinding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegistrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(activityRegistrationBinding.root)
    }
}