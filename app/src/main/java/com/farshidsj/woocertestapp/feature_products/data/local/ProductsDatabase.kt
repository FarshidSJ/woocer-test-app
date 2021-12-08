package com.farshidsj.woocertestapp.feature_products.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farshidsj.woocertestapp.feature_products.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productDao(): ProductsDao

    companion object {
        const val DATABASE_NAME: String = "product_db"
    }
}