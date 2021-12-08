package com.farshidsj.woocertestapp.feature_login.domain.model

import com.farshidsj.woocertestapp.feature_login.data.remote.dto.UserDto

data class AuthenticationModel(
    val name: String,
    val email: String,
    val consumerKey: String,
    val consumerSecret: String
) {
    fun toUserDto(): UserDto {
        return UserDto(
            name = name,
            email = email,
            consumerKey = consumerKey,
            consumerSecret = consumerSecret
        )
    }
}