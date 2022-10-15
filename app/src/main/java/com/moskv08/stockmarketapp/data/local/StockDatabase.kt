package com.moskv08.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class, CompanyInfoEntity::class],
    version = 2 // Increase number when updating definition
)
abstract class StockDatabase: RoomDatabase() {
    // Generate database based on this definition
    abstract val dao: StockDao
}