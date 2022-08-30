package com.moskv08.stockmarketapp.domain.repository

import com.moskv08.stockmarketapp.domain.model.CompanyInfo
import com.moskv08.stockmarketapp.domain.model.CompanyListing
import com.moskv08.stockmarketapp.domain.model.IntradayInfo
import com.moskv08.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfoBySymbol(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfoBySymbol(
        symbol: String
    ): Resource<CompanyInfo>
}