package com.mufiid.up_send.api

import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.data.UserEntity
import com.mufiid.up_send.data.source.remote.response.MessageResponse
import com.mufiid.up_send.data.source.remote.response.WrappedListResponses
import com.mufiid.up_send.data.source.remote.response.WrappedResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ApiServices {
    // LOGIN
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<WrappedResponse<UserEntity>>

    // REGISTRATION
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("username") username: String?,
        @Field("firstname") firstName: String?,
        @Field("lastname") lastName: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<WrappedResponse<UserEntity>>

    // GET EVENTS JOIN
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("event/participant/events")
    fun getListOfEventJoin(
        @Header("Authorization") token: String?,
        @Field("user_id") userId: Int?
    ): Observable<WrappedListResponses<EventEntity>>

    // GET LIST PARTICIPANT JOIN
    @FormUrlEncoded
    @POST("event/participant")
    fun getListOfParticipantJoin(
        @Header("Authorization") token: String?,
        @Field("event_id") eventId: Int?
    ): Observable<WrappedListResponses<UserEntity>>

    // GET LIST PARTICIPANT JOIN
    @FormUrlEncoded
    @POST("event/participant/come")
    fun getListOfParticipantCome(
        @Header("Authorization") token: String?,
        @Field("event_id") eventId: Int?
    ): Observable<WrappedListResponses<UserEntity>>

    // GET EVENTS CREATED
    @GET("event/")
    fun getListOfEventCreated(
        @Header("Authorization") token: String?,
        @Query("user_id") userId: String?
    ): Observable<WrappedListResponses<EventEntity>>

    // GET SHOW EVENT
    @GET("event/show")
    fun getEventById(
        @Header("Authorization") token: String?,
        @Query("id") id: Int?
    ): Observable<WrappedResponse<EventEntity>>

    // GET SEARCH EVENT
    @Headers("Accept: application/json")
    @GET("event/search")
    fun getEventBySearch(
        @Header("Authorization") token: String?,
        @Query("query") query: String?,
        @Query("user_id") userId: Int?
    ): Observable<WrappedListResponses<EventEntity>>

    // GET EVENTS CREATED
    @DELETE("event/{id}")
    fun deleteListOfEventCreated(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Observable<MessageResponse>

    // CREATE EVENT

    // UPDATE EVENT

}