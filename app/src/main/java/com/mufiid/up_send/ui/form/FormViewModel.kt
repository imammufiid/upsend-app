package com.mufiid.up_send.ui.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mufiid.up_send.api.ApiConfig
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FormViewModel : ViewModel() {
    private var state: SingleLiveEvent<FormState> = SingleLiveEvent()
    private var event = MutableLiveData<EventEntity>()
    private val api = ApiConfig.instance()

    fun getEventById(token: String?, eventId: Int?) {
        state.value = FormState.IsLoading(true)
        CompositeDisposable().add(
            api.getEventById("Bearer $token", eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> event.postValue(it.data)
                        else -> state.value = FormState.Error(it.message)
                    }
                    state.value = FormState.IsLoading()
                }, {
                    state.value = FormState.Error(it.message)
                    state.value = FormState.IsLoading()
                })
        )
    }

    fun doSave(
        header: HashMap<String, String>?,
        userId: RequestBody?,
        titleEvent: RequestBody?,
        startDate: RequestBody?,
        dueDate: RequestBody?,
        capacity: RequestBody?,
        description: RequestBody?,
        image: MultipartBody.Part?,
    ) {
        state.value = FormState.IsLoading(true)
        CompositeDisposable().add(
            api.insertEvent(header, userId, titleEvent, startDate, dueDate, capacity, description, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> {
                            state.value =
                                it.message?.let { msg -> FormState.ShowToast(msg) }
                        }
                        else -> {
                            state.value = FormState.IsFailed(it.message)
                        }
                    }
                    state.value = FormState.IsLoading()
                }, {
                    state.value = FormState.IsFailed(it.message)
                    state.value = FormState.IsLoading()
                })

        )
    }

    fun doUpdate(
        header: HashMap<String, String>?,
        eventId: RequestBody?,
        userId: RequestBody?,
        titleEvent: RequestBody?,
        startDate: RequestBody?,
        dueDate: RequestBody?,
        capacity: RequestBody?,
        description: RequestBody?,
        image: MultipartBody.Part?,
    ) {
        state.value = FormState.IsLoading(true)
        CompositeDisposable().add(
            api.updateEvent(header, eventId, userId, titleEvent, startDate, dueDate, capacity, description, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> {
                            state.value =
                                it.message?.let { msg -> FormState.ShowToast(msg) }
                        }
                        else -> {
                            state.value = FormState.IsFailed(it.message)
                        }
                    }
                    state.value = FormState.IsLoading()
                }, {
                    state.value = FormState.IsFailed(it.message)
                    state.value = FormState.IsLoading()
                })

        )
    }

    fun formValidation(
        titleEvent: String?,
        startDate: String?,
        dueDate: String?,
        capacity: String?,
        description: String?,
    ): Boolean {
        state.value = FormState.Reset
        if (titleEvent != null) {
            if (titleEvent.isEmpty()) {
                state.value = FormState.Error("Title Event Tidak Boleh Kosong!")
                return false
            }
        }

        if (startDate != null) {
            if (startDate.isEmpty()) {
                state.value = FormState.Error("Masukkan tanggal mulai")
                return false
            }
        }

        if (dueDate != null) {
            if (dueDate.isEmpty()) {
                state.value = FormState.Error("Masukkan tanggal selesai")
                return false
            }
        }

        if (capacity != null) {
            if (capacity.isEmpty()) {
                state.value = FormState.Error("Masukkan Kapasitas pengunjung")
                return false
            }
        }

        if (description != null) {
            if (description.isEmpty()) {
                state.value = FormState.Error("Masukkan deskripsi")
                return false
            }
        }

        return true
    }
    fun getEvent() = event
    fun getState() = state
}


sealed class FormState {
    data class ShowToast(var message: String?) : FormState()
    data class IsLoading(var state: Boolean? = false) : FormState()
    data class Error(var err: String?) : FormState()
    data class IsSuccess(var user: UserEntity?) : FormState()
    data class IsFailed(var message: String? = null) : FormState()
    data class FormValidation(
        var titleEvent: String? = null,
        var startDate: String? = null,
        var dueDate: String? = null,
        var capacity: String? = null,
        var description: String? = null
    ) : FormState()

    object Reset : FormState()
}