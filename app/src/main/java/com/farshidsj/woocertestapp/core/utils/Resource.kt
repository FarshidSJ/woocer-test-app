package com.farshidsj.woocertestapp.core.utils

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val message: String? = null, val showLoading: Boolean? = false) {
    class Loading<T>(data: T? = null, showLoading: Boolean = false): Resource<T>(data = data, showLoading = showLoading)
    class Success<T>(data: T?): Resource<T>(data, showLoading = false)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message, false)
}