package com.steven.foodqualitydetector.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.steven.foodqualitydetector.data.UploadResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL =
    "https://api-capstone-zhlt4shl3q-as.a.run.app/api/"

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//    .client(httpClient)
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun submitFood(
        @Part("title") title: RequestBody,
        @Part("firebaseToken") firebaseToken: RequestBody,
        @Part image: MultipartBody.Part,
    ): Response<UploadResponse>
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

fun createMultipartBodyPart(name: String, value: String): MultipartBody.Part {
    val requestBody = value.toRequestBody("text/plain".toMediaType())
    return MultipartBody.Part.createFormData(name, value, requestBody)
}
