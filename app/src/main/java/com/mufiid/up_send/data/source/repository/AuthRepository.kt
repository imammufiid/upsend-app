package com.mufiid.up_send.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.data.source.AuthDataSource
import com.mufiid.up_send.data.source.remote.response.RemoteAuthDataSource

class AuthRepository private constructor(private val remoteAuthDataSource: RemoteAuthDataSource) :
    AuthDataSource {
    override fun login(email: String, password: String): LiveData<UserEntity> {
        val dataUser = MutableLiveData<UserEntity>()
        val msg = MutableLiveData<String>()
        remoteAuthDataSource.login(email, password, object: RemoteAuthDataSource.loginCallback {
            override fun onLogin(response: UserEntity) {
                lateinit var user: UserEntity
                if(response != null) {
                    user = UserEntity(
                        response.firstName,
                        response.updatedAt,
                        response.roleId,
                        response.accessId,
                        response.createdAt,
                        response.id,
                        response.email,
                        response.username,
                        response.lastName,
                        response.token
                    )
                    dataUser.postValue(user)
                }
            }

            override fun failedLogin(message: String) {
                msg.postValue(message)
            }

        })
        return dataUser
    }
}