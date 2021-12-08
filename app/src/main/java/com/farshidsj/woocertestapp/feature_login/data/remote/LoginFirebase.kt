package com.farshidsj.woocertestapp.feature_login.data.remote

import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_login.data.remote.dto.UserDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

interface LoginFirebase {

    suspend fun saveUserInformation(
        userDto: UserDto,
        collectionReference: CollectionReference
    ): Resource<UserDto>

}