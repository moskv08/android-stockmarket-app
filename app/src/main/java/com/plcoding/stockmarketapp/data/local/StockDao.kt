package com.plcoding.stockmarketapp.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Delete
    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListing()

    @Query("""
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) like '%' || LOWER(:query) || '%'
        OR UPPER(:query) == symbol
    """)
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}