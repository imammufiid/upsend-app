package com.mufiid.up_send.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mufiid.up_send.R
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.databinding.ActivityLoginBinding
import com.mufiid.up_send.ui.home.HomeActivity
import com.mufiid.up_send.ui.registration.RegistrationActivity
import com.mufiid.up_send.utils.helper.CustomView
import com.mufiid.up_send.utils.pref.UserPref

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSupportActionBar()
        init()
    }

    private fun init() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegistration.setOnClickListener(this)
        
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
        viewModel.getState().observer(this, Observer { 
            handleUIState(it)
        })
    }

    private fun handleUIState(authState: AuthState?) {
        when(authState) {
            is AuthState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is AuthState.IsLoading -> isLoading(authState.state)
            is AuthState.ShowToast -> showToast(authState.message)
            is AuthState.IsSuccess -> isSuccess(authState.user)
            is AuthState.IsFailed -> {
                isLoading(false)
                authState.message?.let { message -> showToast(message) }
            }
            is AuthState.Error -> {
                isLoading(false)
                showToast(authState.err, false)
            }
        }
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

    private fun setPasswordError(err: String?) {
        binding.etPassword.error = err
    }

    private fun setEmailError(err: String?) {
        binding.etEmail.error = err
    }

    private fun isSuccess(user: UserEntity?) {
        user?.let { user -> UserPref.setUserData(this, user) }
        UserPref.setIsLoggedIn(this, true)
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
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
                binding.btnLogin.isEnabled = false
                binding.btnRegistration.isEnabled = false
            } else {
                binding.btnLogin.isEnabled = true
                binding.btnRegistration.isEnabled = true
            }
        }

    }
}