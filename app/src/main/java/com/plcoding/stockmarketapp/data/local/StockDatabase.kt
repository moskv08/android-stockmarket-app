package com.plcoding.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class],
    version = 1 // Increase number when updating definition
)
abstract class StockDatabase: RoomDatabase() {
    // Generate database based on this definition
    abstract val dao: StockDao
}