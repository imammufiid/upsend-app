package com.mufiid.up_send.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.mufiid.up_send.data.EventEntity

data class DetailEventResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class Result(

	@field:SerializedName("data")
	val data: EventEntity,

	@field:SerializedName("participant_is_coming")
	val participantIsComing: Int,

	@field:SerializedName("participant")
	val participant: Int
)
