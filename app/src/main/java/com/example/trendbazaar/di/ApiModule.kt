package com.example.trendbazaar.di

import com.example.trendbazaar.api.ApiService
import com.example.trendbazaar.utils.BASE_URL
import com.example.trendbazaar.utils.TIMEOUT_TIME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object ApiModule
{
    @Provides
    @Singleton
    fun provideUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideGson() : Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideTimeOut() = TIMEOUT_TIME

    @Provides
    @Singleton
    fun provideClient(time : Long) = OkHttpClient.Builder()
        .connectTimeout(time, TimeUnit.SECONDS)
        .readTimeout(time, TimeUnit.SECONDS)
        .writeTimeout(time, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(base_url : String, gson: Gson, client : OkHttpClient) : ApiService =
        Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create(gson))
            .client(client).build().create(ApiService::class.java)
}



