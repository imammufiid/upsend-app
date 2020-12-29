package com.mufiid.up_send.ui.registration

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mufiid.up_send.R
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.databinding.ActivityRegistrationBinding
import com.mufiid.up_send.utils.helper.CustomView
import com.mufiid.up_send.utils.pref.UserPref

class RegistrationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var loading: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        init()
    }

    private fun init() {
        binding.btnRegister.setOnClickListener(this)
        loading = ProgressDialog(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RegistrationViewModel::class.java]

        viewModel.getState().observer(this, Observer {
            handlerUIState(it)
        })
    }

    private fun handlerUIState(state: RegistrationState?) {
        when (state) {
            is RegistrationState.Reset -> {
                setUsernameError(null)
                setFirstNameError(null)
                setLastNameError(null)
                setEmailError(null)
                setPasswordError(null)
            }
            is RegistrationState.IsLoading -> isLoading(state.state)
            is RegistrationState.ShowToast -> showToast(state.message)
            is RegistrationState.IsSuccess -> isSuccess(state.user)
            is RegistrationState.IsFailed -> {
                isLoading(false)
                state.message?.let { message -> showToast(message) }
            }
            is RegistrationState.Error -> {
                isLoading(false)
                showToast(state.err, false)
            }
            is RegistrationState.RegistrationValidation -> {
                state.username?.let { setUsernameError(it) }
                state.firstName?.let { setFirstNameError(it) }
                state.lastName?.let { setLastNameError(it) }
                state.email?.let { setEmailError(it) }
                state.password?.let { setPasswordError(it) }
            }
        }
    }

    private fun setPasswordError(err: String?) {
        binding.etPassword.error = err
    }

    private fun setEmailError(err: String?) {
        binding.etEmail.error = err
    }

    private fun setUsernameError(err: String?) {
        binding.etUsername.error = err
    }

    private fun setFirstNameError(err: String?) {
        binding.etFirstname.error = err
    }

    private fun setLastNameError(err: String?) {
        binding.etLastname.error = err
    }

    private fun isSuccess(user: UserEntity?) {
        CustomView.customToast(this, getString(R.string.registration_success), true,
            isSuccess = true
        )
        Handler(mainLooper).postDelayed({
            finish()
        }, 2000)

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
            if(state) {
                loading.setMessage(getString(R.string.loading))
                loading.setCanceledOnTouchOutside(false)
                loading.show()
            } else {
                loading.dismiss()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                if (viewModel.registrationValidate(
                        binding.etUsername.text.toString(),
                        binding.etFirstname.text.toString(),
                        binding.etLastname.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString(),
                    )
                ) {
                    viewModel.registration(
                        binding.etUsername.text.toString(),
                        binding.etFirstname.text.toString(),
                        binding.etLastname.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString(),
                    )
                }
            }
        }
    }
}