package com.moskv08.stockmarketapp.data.repository

import com.moskv08.stockmarketapp.data.csv.CSVParser
import com.moskv08.stockmarketapp.data.local.StockDatabase
import com.moskv08.stockmarketapp.data.mapper.toCompanyInfo
import com.moskv08.stockmarketapp.data.mapper.toCompanyInfoEntity
import com.moskv08.stockmarketapp.data.mapper.toCompanyListing
import com.moskv08.stockmarketapp.data.mapper.toCompanyListingEntity
import com.moskv08.stockmarketapp.data.remote.StockApi
import com.moskv08.stockmarketapp.domain.model.CompanyInfo
import com.moskv08.stockmarketapp.domain.model.CompanyListing
import com.moskv08.stockmarketapp.domain.model.IntradayInfo
import com.moskv08.stockmarketapp.domain.repository.StockRepository
import com.moskv08.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
): StockRepository {

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {

            emit(Resource.isLoading(true))
            val localListing = db.dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            // Cache is empty and there is also no search filter applied
            val isDbEmpty = localListing.isEmpty() && query.isBlank()

            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.isLoading(false))
                return@flow
            }

            // API Request
            val remoteListing = try {
                val response = api.getCompanyListing()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("There was an exception: ${e.toString()}"))
                null
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("There was an exception: ${e.toString()}"))
                null
            }

            // Update Database
            remoteListing?.let { listings ->
                db.dao.clearCompanyListing()
                db.dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })

                // Read from source
                emit(Resource.Success(
                    data = db.dao.searchCompanyListing("").map { it.toCompanyListing() }
                ))
                emit(Resource.isLoading(false))
            }
        }
    }

    override suspend fun getIntradayInfoBySymbol(symbol: String): Resource<List<IntradayInfo>> {
        // TODO: Load Intraday info only once, afterwards from cache (DB).
        return try {
            val response = api.getIntraDayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            return Resource.Success(results)
        }
        catch (e: IOException){
            e.printStackTrace()
            Resource.Error(message = "Something went wrong: ${e.message}")
        }
        catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(message = "Something went wrong: ${e.message}")
        }
    }

    override suspend fun getCompanyInfoBySymbol(symbol: String): Resource<CompanyInfo> {
        val localResult = db.dao.findCompanyInfo(symbol)

        if(localResult != null){
            val data = localResult.toCompanyInfo()
            return Resource.Success(data)
        }

        // API Request
        return try {
            val dtoResult = api.getCompanyInfo(symbol)
            val companyInfo = dtoResult.toCompanyInfo()

            db.dao.insertCompanyInfo(companyInfo.toCompanyInfoEntity())

            val result = db.dao.findCompanyInfo(symbol)
            return Resource.Success(result.toCompanyInfo())
        }
        catch (e: IOException){
            e.printStackTrace()
            Resource.Error(message = "Something went wrong while trying to get Company Info: ${e.message}")
        }
        catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(message = "Something went wrong while trying to get Company Info: ${e.message}")
        }
    }
}
