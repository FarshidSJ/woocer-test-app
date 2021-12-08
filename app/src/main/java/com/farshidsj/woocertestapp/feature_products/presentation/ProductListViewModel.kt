package com.farshidsj.woocertestapp.feature_products.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel
import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_products.domain.usecase.GetProductList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductList: GetProductList
) : ViewModel() {
    private val TAG = "ProductListViewModel"

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    private val _showLoading = MutableStateFlow(true)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getProducts(consumerKey: String, consumerSecret: String) = viewModelScope.launch {
        getProductList(consumerKey, consumerSecret).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _products.value = it
                    }
                    result.showLoading?.let {
                        _showLoading.value = it
                    }

                }
                is Resource.Error -> {
                    result.data?.let {
                        _products.value = it
                    }
                    result.message?.let {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                it ?: "Unknown error"
                            )
                        )
                    }
                    result.showLoading?.let {
                        _showLoading.value = it
                    }

                }
                is Resource.Loading -> {
                    result.data?.let {
                        _products.value = it
                    }
                    result.showLoading?.let {
                        _showLoading.value = it
                    }

                }
            }

        }.launchIn(this)
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}