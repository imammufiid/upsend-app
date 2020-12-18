package com.mufiid.up_send.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null
)