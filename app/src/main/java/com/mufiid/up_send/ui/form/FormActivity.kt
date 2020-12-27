package com.mufiid.up_send.ui.form

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mufiid.up_send.R
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.databinding.ActivityFormBinding
import com.mufiid.up_send.utils.helper.CustomView
import com.mufiid.up_send.utils.pref.UserPref
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel
    private lateinit var loading: ProgressDialog
    private var imageFromApi: String? = null
    private var currentPhotoPath: String? = null
    private var part: MultipartBody.Part? = null
    private var isUpdate: Boolean? = null
    private var dataEvent: EventEntity? = null

    companion object {
        private const val PERMISSION_CODE = 1001
        private const val IMAGE_PICK_CODE = 1000
        const val IS_UPDATE = "is_update"
        const val EXTRAS_DATA = "extras_data"
        const val START_DATE = "start_date"
        const val DUE_DATE = "due_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        setParcelable()
        checkPermission()
    }

    private fun setParcelable() {
        isUpdate = intent.getBooleanExtra(IS_UPDATE, false)
        dataEvent = intent.getParcelableExtra(EXTRAS_DATA)
        if (isUpdate as Boolean) {
            viewModel.getEventById(
                UserPref.getUserData(this)?.token,
                dataEvent?.id
            )

            viewModel.getEvent().observe(this, Observer {
                if (it != null) {
                    binding.etTitleEvent.setText(it.name)
                    val startDateFromApi = it.startDate?.split(" ")
                    val startDate = startDateFromApi?.get(0)
                    val startTime = startDateFromApi?.get(1)
                    binding.etStartDateEvent.setText(startDate)
                    binding.etStartTimeEvent.setText(startTime)

                    val dueDateFromApi = it.startDate?.split(" ")
                    val dueDate = dueDateFromApi?.get(0)
                    val dueTime = dueDateFromApi?.get(1)
                    binding.etDueDateEvent.setText(dueDate)
                    binding.etDueTimeEvent.setText(dueTime)

                    binding.etCapasityEvent.setText(it.capasity.toString())
                    binding.etDescEvent.setText(it.description)
                    Glide.with(this)
                        .load(it.image)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(binding.imageEvent)
                    imageFromApi = it.image
                }
            })
        }

    }

    private fun init() {
        binding.btnSubmit.setOnClickListener(this)
        binding.btnTakePhoto.setOnClickListener(this)
        binding.include.ibBack.setOnClickListener(this)
        loading = ProgressDialog(this)

        binding.etStartDateEvent.setOnFocusChangeListener { _, b ->
            if (b) {
                showCalendar(START_DATE)
            }
        }
        binding.etDueDateEvent.setOnFocusChangeListener { _, b ->
            if (b) {
                showCalendar(DUE_DATE)
            }
        }

        binding.etStartTimeEvent.setOnFocusChangeListener { _, b ->
            if (b) {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    binding.etStartTimeEvent.setText(SimpleDateFormat("HH:mm").format(cal.time))
                }
                TimePickerDialog(
                    this,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }

        binding.etDueTimeEvent.setOnFocusChangeListener { _, b ->
            if (b) {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    binding.etDueTimeEvent.setText(SimpleDateFormat("HH:mm").format(cal.time))
                }
                TimePickerDialog(
                    this,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FormViewModel::class.java]
        viewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                checkSelfPermission(
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun checkPermissionGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    1
                )
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }


    private fun doSave() {
        val etTitleEvent = binding.etTitleEvent.text.toString()
        val etStartDateTime =
            "${binding.etStartDateEvent.text.toString()} ${binding.etStartTimeEvent.text.toString()}"
        val etDueDateTime =
            "${binding.etDueDateEvent.text.toString()} ${binding.etDueTimeEvent.text.toString()}"
        val etDescription = binding.etDescEvent.text.toString()
        val etCapacity = binding.etCapasityEvent.text.toString()

        val userId = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            UserPref.getUserData(this)?.id.toString()
        )
        val headers = HashMap<String, String>()
        UserPref.getUserData(this)?.token?.let { headers.put("Authorization", "Bearer $it") }


        val titleEvent = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            etTitleEvent
        )
        val startDateTime = RequestBody.create("text/plain".toMediaTypeOrNull(), etStartDateTime)
        val dueDateTime = RequestBody.create("text/plain".toMediaTypeOrNull(), etDueDateTime)
        val capacity = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            etCapacity
        )
        val description = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            etDescription
        )

        if (currentPhotoPath != null) {
            val pictFromBitmap = File(currentPhotoPath)
            val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), pictFromBitmap)
            part = MultipartBody.Part.createFormData("image", pictFromBitmap.name, reqFile)
        } else {
            if (imageFromApi.isNullOrEmpty()) {
                CustomView.customToast(this, "Anda Belum mengunggah foto", true, isSuccess = false)
                return
            }
        }

        if (viewModel.formValidation(
                etTitleEvent,
                etStartDateTime,
                etDueDateTime,
                etCapacity,
                etDescription
            )
        ) {

            if(isUpdate as Boolean) {
                val eventId = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    dataEvent?.id.toString()
                )
                viewModel.doUpdate(headers,
                    eventId,
                    userId,
                    titleEvent,
                    startDateTime,
                    dueDateTime,
                    capacity,
                    description,
                    part
                )
            } else {
                viewModel.doSave(
                    headers,
                    userId,
                    titleEvent,
                    startDateTime,
                    dueDateTime,
                    capacity,
                    description,
                    part
                )
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri!!, filePath, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePath[0])
            val picturePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            currentPhotoPath = picturePath
            binding.imageEvent.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageURI(uri)
            }
        }
    }

    private fun showCalendar(TAG: String) {
        val calender = Calendar.getInstance()
        val datePick = DatePickerDialog(
            this, this,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )

        datePick.datePicker.tag = TAG

        datePick.show()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(v: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val date = calendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = dateFormat.format(date)

        when (v?.tag) {
            START_DATE -> binding.etStartDateEvent.setText(dateStr)
            DUE_DATE -> binding.etDueDateEvent.setText(dateStr)
        }

    }

    private fun handleUIState(it: FormState) {
        when (it) {
            is FormState.Reset -> {
                setTitleEventError(null)
                setStartDateError(null)
                setDueDateError(null)
                setDescriptionError(null)
                setCapacityError(null)
            }
            is FormState.IsLoading -> isLoading(it.state)
            is FormState.ShowToast -> showToast(it.message)
            is FormState.IsFailed -> {
                isLoading(false)
                it.message?.let { message -> showToast(message) }
            }
            is FormState.Error -> {
                isLoading(false)
                showToast(it.err, false)
            }
            is FormState.FormValidation -> {
                it.titleEvent?.let {
                    setTitleEventError(it)
                }
                it.startDate?.let {
                    setStartDateError(it)
                }
                it.dueDate?.let {
                    setDueDateError(it)
                }
                it.capacity?.let {
                    setCapacityError(it)
                }
                it.description?.let {
                    setDescriptionError(it)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_take_photo -> {
                checkPermissionGallery()
            }
            R.id.btn_submit -> {
                doSave()
            }
            R.id.ib_back -> {
                finish()
            }
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

    private fun isLoading(state: Boolean?) {
        state?.let { state ->
            if (state) {
                loading.setMessage("Loading...")
                loading.setCanceledOnTouchOutside(false)
                loading.show()
            } else {
                loading.dismiss()
            }
        }
    }

    private fun setDescriptionError(it: String?) {
        binding.etDescEvent.error = it
    }

    private fun setCapacityError(it: String?) {
        binding.etCapasityEvent.error = it
    }

    private fun setDueDateError(it: String?) {
        binding.etDueDateEvent.error = it
        binding.etDueTimeEvent.error = it
    }

    private fun setStartDateError(it: String?) {
        binding.etStartDateEvent.error = it
        binding.etDueTimeEvent.error = it
    }

    private fun setTitleEventError(it: String?) {
        binding.etTitleEvent.error = it
    }

}