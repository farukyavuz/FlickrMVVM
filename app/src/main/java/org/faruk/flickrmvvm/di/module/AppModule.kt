package org.faruk.flickrmvvm.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.faruk.flickrmvvm.common.API_KEY
import org.faruk.flickrmvvm.common.BASE_URL
import org.faruk.flickrmvvm.network.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun providesAppContext() = appContext

//    @Provides
//    @Singleton
//    fun provideHttpCache(application: Application): Cache {
//        val cacheSize: Long = 10 * 1024 * 1024
//        return Cache(application.cacheDir, cacheSize)
//    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

//    @Provides
//    @Singleton
//    fun provideOkhttpClient(cache: Cache): OkHttpClient {
//        val client = OkHttpClient.Builder()
//        client.cache(cache)
//        return client.build()
//    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor { chain ->
            val mOriginal = chain.request()
            val mUrl = mOriginal.url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val requestBuilder = mOriginal.newBuilder()
                .url(mUrl)
            chain.proceed(requestBuilder.build())
        }
        return client.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideFlickrApi(retrofit: Retrofit): FlickrApi {
        return retrofit.create(FlickrApi::class.java)
    }
}