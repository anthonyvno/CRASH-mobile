package com.example.anthonyvannoppen.androidproject.injection.module

import com.example.anthonyvannoppen.androidproject.network.HubApi
import com.example.anthonyvannoppen.androidproject.utils.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Module which provides all required dependencies for the network.
 *
 * Object: Singleton Instance see [The Kotlin reference](https://kotlinlang.org/docs/reference/object-declarations.html)
 * Retrofit: Library used for REST connections. See [The Retrofit reference](https://square.github.io/retrofit/)
 * What is Dependency Injection? See this [video](https://www.youtube.com/watch?v=IKD2-MAkXyQ)
 *
 * Methods annotated with @Provides informs Dagger that this method can provide a certain dependency.
 * Methods annotated with @Singleton indicate that Dagger should only instantiate the dependency
 *  once and provide that some object on further requests.
 */
@Module
object NetworkModule {


    /**
     * @param retrofit the retrofit object used to instantiate the service
     */
    @Provides
    @Singleton
    internal fun provideHubApi(retrofit: Retrofit): HubApi {
        return retrofit.create(HubApi::class.java)
    }


    /**
     * Return the Retrofit object.
     * To fully configure Retrofit we require a HTTP client (okHTTP),
     * a converterFactory that can create a converter that parses JSON into model objects, and
     * a callAdapterFactory that can create an Adapter that allows us to use RxJava
     *  to handle async requests instead of the Calls that Retrofit provides itself.
     *
     * The function paramaters are interfaces, not the types of the specific kind of factories.
     * This allows us to easily swap out different kind of factories.
     */
    @Provides
    @Singleton
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient,
                                          converterFactory: retrofit2.Converter.Factory,
                                          callAdapterFactory: retrofit2.CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()
    }

    /**
     * Returns the OkHttpClient.
     * RetroFit uses OkHTTP by default and we normally don't have to add the client explicitly
     * to the builder. We slightly modify the client however to intercept calls and log them
     * for easier debugging.
     */
    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        //To debug Retrofit/OkHttp we can intercept the calls and log them.
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
    }

    /**
     * The return type specifies the Factory interface.
     * Currently we choose to use a MoshiConverterFactory,
     * but this choice can easily be changed without needing any further changes.
     */
    @Provides
    @Singleton
    internal fun provideJSONConverter(): retrofit2.Converter.Factory {
        return MoshiConverterFactory.create()
    }

    /**
     * Here the return type is the interface as well.
     * We choose to create an RxJavaCallAdapterFactory, but changing this is easily done
     * without requiring further changes.
     */
    @Provides
    @Singleton
    internal fun provideCallAdapterFactory(): retrofit2.CallAdapter.Factory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }
}