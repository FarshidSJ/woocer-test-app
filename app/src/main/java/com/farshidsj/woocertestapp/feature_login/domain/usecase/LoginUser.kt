package com.farshidsj.woocertestapp.feature_login.domain.usecase

import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import com.farshidsj.woocertestapp.feature_login.domain.repository.LoginRepository
import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUser(
    private val repository: LoginRepository
) {

    operator fun invoke(authenticationModel: AuthenticationModel, collectionName: String): Flow<Resource<AuthenticationModel>> {
        if (collectionName.isBlank()) {
            return flow {  }
        }
        return repository.saveUserInformation(authenticationModel, collectionName)
    }

}