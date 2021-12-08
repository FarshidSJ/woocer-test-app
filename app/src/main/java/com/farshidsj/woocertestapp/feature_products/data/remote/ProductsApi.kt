package com.farshidsj.woocertestapp.feature_products.data.remote

import com.farshidsj.woocertestapp.feature_products.data.remote.dto.ProductListDto
import retrofit2.http.GET

interface ProductsApi {

    @GET("wp-json/wc/v3/products/tags")
    suspend fun getProductList(): ProductListDto

}