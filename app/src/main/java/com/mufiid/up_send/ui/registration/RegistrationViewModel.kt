package com.mufiid.up_send.ui.registration

import androidx.lifecycle.ViewModel
import com.mufiid.up_send.api.ApiConfig
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.ui.login.AuthState
import com.mufiid.up_send.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RegistrationViewModel:ViewModel() {

    private var state: SingleLiveEvent<RegistrationState> = SingleLiveEvent()
    private val api = ApiConfig.instance()

    fun registration(
        username: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?
    ) {
        state.value = RegistrationState.IsLoading(true)
        CompositeDisposable().add(
            api.register(username, firstName, lastName, email, password, 1, 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> {
                            state.value =
                                it.data?.let { data -> RegistrationState.IsSuccess(data) }
                        }
                        else -> {
                            state.value = RegistrationState.IsFailed(it.message)
                        }
                    }
                    state.value = RegistrationState.IsLoading()
                }, {
                    state.value = RegistrationState.IsFailed(it.message)
                    state.value = RegistrationState.IsLoading()
                })

        )
    }

    fun registrationValidate(
        username: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
    ): Boolean {
        state.value = RegistrationState.Reset

        if (firstName != null) {
            if (firstName.isEmpty()) {
                state.value = RegistrationState.Error("Nama Depan Tidak Boleh Kosong!")
                return false
            }
        }

        if (lastName != null) {
            if (lastName.isEmpty()) {
                state.value = RegistrationState.Error("Nama Belakang Tidak Boleh Kosong!")
                return false
            }
        }

        if (username != null) {
            if (username.isEmpty()) {
                state.value = RegistrationState.Error("Username Tidak Boleh Kosong!")
                return false
            }
        }

        if (email != null) {
            if (email.isEmpty()) {
                state.value = RegistrationState.Error("Email Tidak Boleh Kosong!")
                return false
            }
        }

        if (password != null) {
            if (password.isEmpty()) {
                state.value = RegistrationState.Error("Password Tidak Boleh Kosong")
                return false
            }
        }

        return true
    }

    fun getState() = state
}

sealed class RegistrationState {
    data class ShowToast(var message: String?) : RegistrationState()
    data class IsLoading(var state: Boolean? = false) : RegistrationState()
    data class Error(var err: String?) : RegistrationState()
    data class IsSuccess(var user: UserEntity?) : RegistrationState()
    data class IsFailed(var message: String? = null) : RegistrationState()
    data class RegistrationValidation(
        var username: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var email: String? = null,
        var password: String? = null
    ) : RegistrationState()

    object Reset : RegistrationState()
}