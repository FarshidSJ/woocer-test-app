package com.farshidsj.woocertestapp.feature_products.data.repository

import com.farshidsj.woocertestapp.feature_products.data.remote.ProductsApi
import com.farshidsj.woocertestapp.feature_products.data.local.ProductsDao
import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel
import com.farshidsj.woocertestapp.feature_products.domain.repository.ProductsRepository
import com.farshidsj.woocertestapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ProductsRepositoryImpl
constructor(
    private val productsApi: ProductsApi,
    private val productsDao: ProductsDao
) : ProductsRepository {
    private val TAG = "MainRepositoryImpl"

    override fun getProductList(
        consumerKey: String,
        consumerSecret: String
    ): Flow<Resource<List<ProductModel>>> = flow {
        emit(Resource.Loading(showLoading = true))

        val products =
            productsDao.getProducts().map { it.toProductModel() }
        if (products.isEmpty()) {
            emit(Resource.Loading(data = products, showLoading = true))
        } else {
            emit(Resource.Loading(data = products, showLoading = false))
        }


        try {
            val remoteProducts = productsApi.getProductList()

            productsDao.deleteProducts(remoteProducts.map { it.id })
            productsDao.insertProducts(remoteProducts.map { it.toProductEntity() })

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = products
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = products
                )
            )
        }

        val newProducts = productsDao.getProducts().map { it.toProductModel() }
        emit(Resource.Success(newProducts))
    }

}