package com.farshidsj.woocertestapp.feature_login.data.repository

import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_login.data.remote.LoginFirebase
import com.farshidsj.woocertestapp.feature_login.data.remote.dto.UserDto
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import com.farshidsj.woocertestapp.feature_login.domain.repository.LoginRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginRepositoryImpl
constructor(
    private val firebaseFirestore: FirebaseFirestore
) : LoginRepository {
    override fun saveUserInformation(authenticationModel: AuthenticationModel, collectionName: String): Flow<Resource<AuthenticationModel>> = flow {

        emit(Resource.Loading())
        try {
            val userDto = authenticationModel.toUserDto()
            val collectionReference = firebaseFirestore.collection(collectionName)
            collectionReference.add(userDto).await()
            emit(Resource.Success(authenticationModel))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error("Oops, something went wrong!", authenticationModel))
        }

    }
}