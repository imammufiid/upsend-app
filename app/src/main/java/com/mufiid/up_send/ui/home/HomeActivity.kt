package com.mufiid.up_send.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufiid.up_send.R
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.databinding.ActivityHomeBinding
import com.mufiid.up_send.ui.detail.DetailActivity
import com.mufiid.up_send.ui.event.EventActivity
import com.mufiid.up_send.utils.helper.CustomView
import com.mufiid.up_send.utils.pref.UserPref

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // view pager tabs
//        val pagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
//        activityHomeBinding.viewPager.adapter = pagerAdapter
//        activityHomeBinding.tabs.setupWithViewPager(activityHomeBinding.viewPager)
        initSupportActionBar()
        init()
        showDataEvent()
        setRecyclerView()
        showDataBySearch()
    }

    override fun onResume() {
        super.onResume()
        binding.include.svHome.setQuery("", false)
        binding.include.svHome.isIconified = true
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
        viewModel.getState().observer(this, Observer {
            handlerUIState(it)
        })

        binding.include.ibScan.setOnClickListener(this)
        binding.include.ibSetting.setOnClickListener(this)
        binding.include.ibAdd.setOnClickListener(this)
    }

    private fun handlerUIState(it: EventState?) {
        when (it) {
            is EventState.IsLoading -> showLoading(it.state)
            is EventState.Error -> showToast(it.err)
        }
    }

    private fun setRecyclerView() {
        adapter = EventAdapter { event ->
            showSelectedData(event)
        }.apply {
            notifyDataSetChanged()
        }

        binding.rvEvents.layoutManager = LinearLayoutManager(this)
        binding.rvEvents.adapter = adapter
    }

    private fun showDataEvent() {
        UserPref.getUserData(this)?.let {
            it.token?.let { it1 -> Log.d("TOKEN", it1) }
            viewModel.getAllDataByJoin(it.id, it.token)
        }

        viewModel.getEvents().observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text = getString(R.string.data_not_found)
            } else {
                binding.tvMessage.visibility = View.GONE
                adapter.setEvent(it)
            }
        })
    }

    private fun showDataBySearch() {
        binding.include.svHome.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getEventsBySearch(
                    UserPref.getUserData(this@HomeActivity)?.token,
                    query,
                    UserPref.getUserData(this@HomeActivity)?.id
                )
                binding.include.svHome.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        binding.include.svHome.setOnCloseListener {
            showDataEvent()
            return@setOnCloseListener false
        }
    }

    private fun initSupportActionBar() {
        setSupportActionBar(binding.include.toolbar)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_setting -> {
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
            }
            R.id.ib_scan -> {
                Toast.makeText(this, "SCAN", Toast.LENGTH_SHORT).show()
            }
            R.id.ib_add -> {
                startActivity(Intent(this, EventActivity::class.java))
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String?, state: Boolean? = true) {
        state?.let { isSuccess ->
            if (isSuccess) {
                CustomView.customToast(this, message, true, isSuccess = true)
            } else {
                CustomView.customToast(this, message, true, isSuccess = false)
            }
        }
    }

    private fun showSelectedData(event: EventEntity) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRAS_DATA, event)
        })
    }
}