package com.farshidsj.woocertestapp.feature_login.di

import com.farshidsj.woocertestapp.feature_login.data.repository.LoginRepositoryImpl
import com.farshidsj.woocertestapp.feature_login.domain.repository.LoginRepository
import com.farshidsj.woocertestapp.feature_login.domain.usecase.LoginUser
import com.farshidsj.woocertestapp.feature_products.domain.repository.ProductsRepository
import com.farshidsj.woocertestapp.feature_products.domain.usecase.GetProductList
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginUserUseCase(repository: LoginRepository): LoginUser {
        return LoginUser(repository)
    }

    @Provides
    @Singleton
    fun provideLoginFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        firebaseFirestore: FirebaseFirestore
    ): LoginRepository {
        return LoginRepositoryImpl(firebaseFirestore)
    }

}