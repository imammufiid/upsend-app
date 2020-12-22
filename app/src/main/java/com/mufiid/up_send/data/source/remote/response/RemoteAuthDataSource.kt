package com.mufiid.up_send.data.source.remote.response

import com.mufiid.up_send.api.ApiConfig
import com.mufiid.up_send.data.UserEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RemoteAuthDataSource {
    companion object {
        @Volatile
        private var instance: RemoteAuthDataSource? = null

        fun getInstance(): RemoteAuthDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteAuthDataSource()
            }
    }

    fun login(email: String?, password: String?, callback: loginCallback) {
        CompositeDisposable().add(
            email?.let { email ->
                password?.let { password ->
                    ApiConfig.instance().login(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            when(it.status) {
                                200 -> it.data?.let { data -> callback.onLogin(data) }
                                else -> it.message?.let { message -> callback.failedLogin(message) }
                            }
                        }, {
                            it.message?.let { message -> callback.failedLogin(message) }
                        })
                }
            }
        )
    }

    interface loginCallback {
        fun onLogin(response: UserEntity)
        fun failedLogin(message: String)
    }
}