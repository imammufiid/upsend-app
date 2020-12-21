package com.mufiid.up_send.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NavUtils
import com.mufiid.up_send.R
import com.mufiid.up_send.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityDetailBinding.layoutParticipantRegistration.setOnClickListener {
            Toast.makeText(this, "Participant registration", Toast.LENGTH_SHORT).show()
        }
        activityDetailBinding.layoutParticipantCome.setOnClickListener {
            Toast.makeText(this, "Participant Come", Toast.LENGTH_SHORT).show()
        }
        activityDetailBinding.layoutScan.setOnClickListener {
            Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}