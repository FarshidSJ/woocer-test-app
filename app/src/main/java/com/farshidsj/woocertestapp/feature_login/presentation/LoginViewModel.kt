package com.farshidsj.woocertestapp.feature_login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import com.farshidsj.woocertestapp.feature_login.domain.usecase.LoginUser
import com.farshidsj.woocertestapp.feature_products.presentation.ProductListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUser: LoginUser
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthenticationModel>(AuthenticationModel("", "", "", ""))
    val loginState: StateFlow<AuthenticationModel> = _loginState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun performLogin(authenticationModel: AuthenticationModel, collectionName: String) =
        viewModelScope.launch {
            loginUser(authenticationModel, collectionName).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _loginState.value = it
                        }
                    }
                    is Resource.Error -> {
                        result.data?.let {
                            _loginState.value = it
                        }
                        result.message?.let {
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                it ?: "Unknown error"
                            ))
                        }

                    }
                    is Resource.Loading -> {

                    }
                }
            }.launchIn(this)
        }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String): UIEvent()
    }

}