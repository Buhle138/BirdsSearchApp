package com.example.birdssearchapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

//Image generator image service
private  val retrofitImage = Retrofit.Builder().baseUrl("https://api.ebird.org/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val birdsResponse = retrofitImage.create(ImageApiService::class.java)

interface ImageApiService{
    @Headers("X-eBirdApiToken: gi422aer30qs")
    @GET("data/obs/{region}/recent")
    suspend fun searchRegion(
        @Path("region") region: String
    ) : List<Root2>
}