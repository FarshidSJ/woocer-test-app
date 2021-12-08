package com.farshidsj.woocertestapp.feature_products.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farshidsj.woocertestapp.feature_products.domain.model.ProductModel

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val description: String,
    val slug: String
) {
    fun toProductModel(): ProductModel {
        return ProductModel(
            id = id,
            name = name,
            description = description
        )
    }
}