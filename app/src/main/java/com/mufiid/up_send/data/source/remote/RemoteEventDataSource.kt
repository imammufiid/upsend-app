package com.mufiid.up_send.data.source.remote

class RemoteEventDataSource {

    companion object {
        @Volatile
        private var instance: RemoteEventDataSource? = null

        fun getInstance(): RemoteEventDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteEventDataSource()
            }
    }

}