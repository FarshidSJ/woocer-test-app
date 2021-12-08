package com.farshidsj.woocertestapp.feature_products.domain.usecase

import com.farshidsj.woocertestapp.core.utils.Resource
import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel
import com.farshidsj.woocertestapp.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProductList(
    private val repository: ProductsRepository
) {

    operator fun invoke(
        consumerKey: String,
        consumerSecret: String
    ): Flow<Resource<List<ProductModel>>> {
        if (consumerKey.isBlank() || consumerSecret.isBlank()) {
            return flow { }
        }
        return repository.getProductList(consumerKey, consumerSecret)
    }
}