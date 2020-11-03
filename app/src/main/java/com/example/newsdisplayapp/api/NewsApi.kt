package com.example.newsdisplayapp.api

import com.example.newsdisplayapp.model.NewsModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi{

    @GET("index.aspx")
    fun getTimeSeriesDaily(
        @Query("records") records:String,
        @Query("action") action:String ="view",
        @Query("activity") activity:String="stocknews"
    ):Call<NewsModel>
}