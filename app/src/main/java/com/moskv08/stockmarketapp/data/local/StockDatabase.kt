package com.moskv08.stockmarketapp.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 4, // Increase number when updating definition
    entities = [CompanyListingEntity::class, CompanyInfoEntity::class],
    autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true
)
abstract class StockDatabase: RoomDatabase() {
    // Generate database based on this definition
    abstract val dao: StockDao
}