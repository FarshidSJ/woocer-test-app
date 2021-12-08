package com.farshidsj.woocertestapp.feature_products.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.farshidsj.woocertestapp.core.utils.AppPreferences
import com.farshidsj.woocertestapp.core.utils.Constants
import com.farshidsj.woocertestapp.feature_products.data.local.ProductsDatabase
import com.farshidsj.woocertestapp.feature_products.data.remote.ProductsApi
import com.farshidsj.woocertestapp.feature_products.data.repository.ProductsRepositoryImpl
import com.farshidsj.woocertestapp.feature_products.domain.repository.ProductsRepository
import com.farshidsj.woocertestapp.feature_products.domain.usecase.GetProductList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    @Singleton
    fun provideGetProductListUseCase(repository: ProductsRepository): GetProductList {
        return GetProductList(repository)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        db: ProductsDatabase,
        api: ProductsApi
    ): ProductsRepository {
        return ProductsRepositoryImpl(api, db.productDao())
    }

    @Provides
    @Singleton
    fun provideProductsDatabase(app: Application): ProductsDatabase {
        return Room.databaseBuilder(
            app, ProductsDatabase::class.java, "product_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext appContext: Context): AppPreferences =
        AppPreferences(appContext)

    @Provides
    @Singleton
    fun provideOkHttpClient(appPreferences: AppPreferences): OkHttpClient {

        return runBlocking {
            var consumerKey: String
            var consumerSecret: String
            withContext(Dispatchers.Default) {
                consumerKey = appPreferences.getAuthForm().first().consumerKey
                consumerSecret = appPreferences.getAuthForm().first().consumerSecret
            }
            val consumer = OkHttpOAuthConsumer(
                consumerKey,
                consumerSecret
            )
            return@runBlocking OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideProductsApi(okHttpClient: OkHttpClient): ProductsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ProductsApi::class.java)
    }

}