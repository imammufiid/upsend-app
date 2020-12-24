package com.mufiid.up_send.ui.detail

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mufiid.up_send.R
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.databinding.ActivityDetailBinding
import com.mufiid.up_send.ui.participant.ParticipantListDialogFragment
import com.mufiid.up_send.utils.helper.CustomView
import com.mufiid.up_send.utils.pref.UserPref

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var loading: ProgressDialog
    private var dataEvent: EventEntity? = null

    companion object {
        const val EXTRAS_DATA = "extras_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        setParcelable()
    }

    private fun init() {
        binding.layoutParticipantRegistration.setOnClickListener(this)
        binding.layoutParticipantCome.setOnClickListener(this)
        binding.layoutScan.setOnClickListener(this)
        loading = ProgressDialog(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]
        viewModel.getState().observer(this, Observer {
            handlerUIState(it)
        })
    }

    private fun handlerUIState(it: DetailState?) {
        when (it) {
            is DetailState.IsLoading -> showLoading(it.state)
            is DetailState.Error -> showToast(it.err, false)
        }
    }

    private fun setParcelable() {
        dataEvent = intent.getParcelableExtra(EXTRAS_DATA)
        dataEvent?.let {

            viewModel.getEventById(UserPref.getUserData(this)?.token, it.id)
        }

        viewModel.getEvent().observe(this, Observer {
            if (it != null) {
                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binding.imageEvent)
                binding.titleEvent.text = it.name
                binding.dateTimeEvent.text = it.startDate
                binding.dueDateTimeEvent.text = it.dueDate
                binding.descEvent.text = it.description
                binding.countParticipantRegistration.text = it.participant.toString()
                binding.countParticipantCome.text = it.participantIsComing.toString()
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            loading.setMessage("Loading...")
            loading.setCanceledOnTouchOutside(false)
            loading.show()
        } else {
            loading.dismiss()
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_participant_registration -> {
                val bundle = Bundle().apply {
                    dataEvent?.id?.let { putInt(ParticipantListDialogFragment.EVENT_ID, it) }
                    putBoolean(ParticipantListDialogFragment.IS_COMING, false)
                }
                ParticipantListDialogFragment().apply {
                    arguments = bundle
                    show(supportFragmentManager, ParticipantListDialogFragment.TAG)
                }
            }
            R.id.layout_participant_come -> {
                val bundle = Bundle().apply {
                    dataEvent?.id?.let { putInt(ParticipantListDialogFragment.EVENT_ID, it) }
                    putBoolean(ParticipantListDialogFragment.IS_COMING, true)
                }
                ParticipantListDialogFragment().apply {
                    arguments = bundle
                    show(supportFragmentManager, ParticipantListDialogFragment.TAG)
                }
            }
            R.id.layout_scan -> {
                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show()
            }
        }

    }
}