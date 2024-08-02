package com.example.apis

import retrofit2.Call
import retrofit2.http.GET

interface apiInterface {

    @GET("posts")
    fun getdata(): Call<List<mydataItem>>
}