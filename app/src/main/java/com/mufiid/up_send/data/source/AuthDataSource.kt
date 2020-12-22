package com.mufiid.up_send.data.source

import androidx.lifecycle.LiveData
import com.mufiid.up_send.data.UserEntity

interface AuthDataSource {
    fun login(email: String, password: String): LiveData<UserEntity>
}