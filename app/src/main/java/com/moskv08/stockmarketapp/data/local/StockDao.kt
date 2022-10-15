package com.moskv08.stockmarketapp.data.local

import androidx.room.*
import retrofit2.http.GET

@Dao
interface StockDao {

    /*
    * Company Listings
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListing()

    @Query(
        """
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) like '%' || LOWER(:query) || '%'
        OR UPPER(:query) == symbol
    """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>

    /*
    * Company Info
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyInfo(
        companyInfoEntity: CompanyInfoEntity
    )

    @Query("SELECT * FROM CompanyInfoEntity WHERE UPPER(:query) == symbol")
    suspend fun findCompanyInfo(query: String): CompanyInfoEntity
}