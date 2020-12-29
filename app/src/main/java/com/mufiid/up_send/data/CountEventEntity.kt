package com.mufiid.up_send.data

import com.google.gson.annotations.SerializedName

data class CountEventEntity(

	@field:SerializedName("count_event_join")
	val countEventJoin: Int? = null,

	@field:SerializedName("count_event_created")
	val countEventCreated: Int? = null
)
