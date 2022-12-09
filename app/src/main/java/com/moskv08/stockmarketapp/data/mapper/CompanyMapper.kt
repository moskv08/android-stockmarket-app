package com.moskv08.stockmarketapp.data.mapper

import com.moskv08.stockmarketapp.data.local.CompanyInfoEntity
import com.moskv08.stockmarketapp.data.local.CompanyListingEntity
import com.moskv08.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.moskv08.stockmarketapp.domain.model.CompanyInfo
import com.moskv08.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

// Write to DB
fun CompanyInfo.toCompanyInfoEntity(): CompanyInfoEntity {
    return CompanyInfoEntity(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry
    )
}

// Read from DB
fun CompanyInfoEntity.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "N/A",
        description = description ?: "N/A",
        name = name ?: "N/A",
        country = country ?: "N/A",
        industry = industry ?: "N/A"
    )
}

// Load from API
fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}