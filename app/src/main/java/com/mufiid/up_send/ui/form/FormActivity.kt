package com.mufiid.up_send.ui.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mufiid.up_send.R
import com.mufiid.up_send.databinding.ActivityFormBinding

class FormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
    }

    private fun init() {
        binding.btnSubmit.setOnClickListener(this)
        binding.btnTakePhoto.setOnClickListener(this )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_take_photo -> {}
            R.id.btn_submit -> {}
        }
    }
}