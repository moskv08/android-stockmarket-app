package com.plcoding.stockmarketapp.data.repository

import com.plcoding.stockmarketapp.data.csv.CSVParser
import com.plcoding.stockmarketapp.data.local.StockDao
import com.plcoding.stockmarketapp.data.local.StockDatabase
import com.plcoding.stockmarketapp.data.mapper.toCompanyListing
import com.plcoding.stockmarketapp.data.mapper.toCompanyListingEntity
import com.plcoding.stockmarketapp.data.remote.StockApi
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
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
    private val companyListingParser: CSVParser<CompanyListing>
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
                val response = api.getStockData()
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
}
