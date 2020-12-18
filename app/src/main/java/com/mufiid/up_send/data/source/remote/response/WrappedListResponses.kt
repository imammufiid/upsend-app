package com.mufiid.up_send.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class WrappedListResponses<T>(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("data") var data: List<T>? = null
)