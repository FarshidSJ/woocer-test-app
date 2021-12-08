package com.farshidsj.woocertestapp.feature_login.domain.repository

import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_login.data.remote.dto.UserDto
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun saveUserInformation(authenticationModel: AuthenticationModel, collectionName: String) : Flow<Resource<AuthenticationModel>>

}