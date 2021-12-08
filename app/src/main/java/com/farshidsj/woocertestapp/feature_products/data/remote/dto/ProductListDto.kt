package com.farshidsj.woocertestapp.feature_products.data.remote.dto


import com.farshidsj.woocertestapp.feature_products.data.local.entity.ProductEntity
import com.google.gson.annotations.SerializedName

class ProductListDto : ArrayList<ProductListDto.ProductListDtoItem>(){
    data class ProductListDtoItem(
        @SerializedName("count")
        val count: Int,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("_links")
        val links: Links,
        @SerializedName("name")
        val name: String,
        @SerializedName("slug")
        val slug: String
    ) {
        data class Links(
            @SerializedName("collection")
            val collection: List<Collection>,
            @SerializedName("self")
            val self: List<Self>
        ) {
            data class Collection(
                @SerializedName("href")
                val href: String
            )
    
            data class Self(
                @SerializedName("href")
                val href: String
            )
        }
        fun toProductEntity() : ProductEntity {
            return ProductEntity(
                id = id,
                name = name,
                description = description,
                slug = slug
            )
        }

    }

}