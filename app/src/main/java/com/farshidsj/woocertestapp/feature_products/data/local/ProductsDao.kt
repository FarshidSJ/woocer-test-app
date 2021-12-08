package com.farshidsj.woocertestapp.feature_products.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farshidsj.woocertestapp.feature_products.data.local.entity.ProductEntity

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM productEntity WHERE id IN (:ids)")
    suspend fun deleteProducts(ids: List<Int>)

    @Query("SELECT * FROM ProductEntity")
    suspend fun getProducts(): List<ProductEntity>
}