package com.bumsoft.jokes.service.repository

import com.bumsoft.jokes.model.StoryModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerRepository {

    @GET("api/stories/random?")
    fun getRandomStory(
            @Query("device_id") deviceId: String,
            @Query("count") count: Int): Observable<StoryModel>

    @POST("api/stories/{code}/rating?")
    fun setRating(@Path("code") code: Long, @Query("reaction") reaction: String): Observable<StoryModel>
}
