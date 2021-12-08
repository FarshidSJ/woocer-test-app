package com.farshidsj.woocertestapp.feature_products.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.farshidsj.woocertestapp.core.utils.AppPreferences
import com.farshidsj.woocertestapp.core.utils.Constants
import com.farshidsj.woocertestapp.core.utils.Utils
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
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
        var consumerKey = Utils.consumerKey
        var consumerSecret = Utils.consumerSecret
        var consumer = OkHttpOAuthConsumer(
            consumerKey,
            consumerSecret
        )
        return OkHttpClient.Builder()
            .addInterceptor(SigningInterceptor(consumer))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
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

    /*@Singleton
    @Provides
    fun provideMainRepository(
        productsApi: ProductsApi,
        productDao: ProductDao
    ): ProductsRepositoryImpl {
        return ProductsRepositoryImpl(productsApi, productDao)
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
//            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        var consumer = OkHttpOAuthConsumer(
            "ck_85f212310cfff32728cc4c933331aa6bcf3002ef",
            "cs_ee784168289012a919a008985d2252fadecea2bb"
        )

        return OkHttpClient.Builder()
            .addInterceptor(SigningInterceptor(consumer))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    }


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideBlogService(retrofit: Retrofit.Builder): ProductsApi {
        return retrofit
            .build()
            .create(ProductsApi::class.java)
    }


    @Singleton
    @Provides
    fun provideProductDB(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            ProductDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideProductDAO(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.productDao()
    }*/

}