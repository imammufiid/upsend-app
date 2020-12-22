package com.mufiid.up_send.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventEntity(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("event_id")
	val eventId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("capasity")
	val capasity: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("due_date")
	val dueDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_user_come")
	val isUserCome: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
): Parcelable
