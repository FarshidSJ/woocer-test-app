package com.farshidsj.woocertestapp.feature_login.domain.model

data class AuthenticationModel(
    val name: String,
    val email: String,
    val consumerKey: String,
    val consumerSecret: String
)