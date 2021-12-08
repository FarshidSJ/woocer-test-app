package com.farshidsj.woocertestapp.feature_products.domain.repository

import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel
import com.farshidsj.woocertestapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProductList(consumerKey: String, consumerSecret: String): Flow<Resource<List<ProductModel>>>
}