package com.moskv08.stockmarketapp.di

import com.moskv08.stockmarketapp.data.csv.CSVParser
import com.moskv08.stockmarketapp.data.csv.CompanyListingsParser
import com.moskv08.stockmarketapp.data.csv.IntradayInfoParser
import com.moskv08.stockmarketapp.data.repository.StockRepository
import com.moskv08.stockmarketapp.domain.model.CompanyListing
import com.moskv08.stockmarketapp.domain.model.IntradayInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepository: StockRepository
    ): com.moskv08.stockmarketapp.domain.repository.StockRepository
}