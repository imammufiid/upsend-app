package com.mufiid.up_send.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mufiid.up_send.R
import com.mufiid.up_send.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var activityHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)
        setSupportActionBar(activityHomeBinding.include.toolbar)

        // view pager tabs
//        val pagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
//        activityHomeBinding.viewPager.adapter = pagerAdapter
//        activityHomeBinding.tabs.setupWithViewPager(activityHomeBinding.viewPager)


        // action listener icon scan and setting
        activityHomeBinding.include.ibScan.setOnClickListener {
            Toast.makeText(this, "SCAN", Toast.LENGTH_SHORT).show()
        }

        activityHomeBinding.include.ibSetting.setOnClickListener {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
        }
    }
}