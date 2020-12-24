package com.mufiid.up_send.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mufiid.up_send.api.ApiConfig
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.ui.home.EventState
import com.mufiid.up_send.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailViewModel() : ViewModel() {
    private var event = MutableLiveData<EventEntity>()
    private var state: SingleLiveEvent<DetailState> = SingleLiveEvent()
    private var api = ApiConfig.instance()


    fun getEventById(token: String?, eventId: Int?) {
        state.value = DetailState.IsLoading(true)
        CompositeDisposable().add(
            api.getEventById("Bearer $token", eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it.status) {
                        200 -> event.postValue(it.data)
                        else -> state.value = DetailState.Error(it.message)
                    }
                    state.value = DetailState.IsLoading()
                }, {
                    state.value = DetailState.Error(it.message)
                    state.value = DetailState.IsLoading()
                })
        )
    }

    fun getEvent() = event
    fun getState() = state
}

sealed class DetailState() {
    data class IsLoading(var state: Boolean = false): DetailState()
    data class Error(var err: String?): DetailState()
}