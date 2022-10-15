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

fun CompanyInfo.toCompanyInfoEntity(): CompanyInfoEntity {
    return CompanyInfoEntity(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry
    )
}

fun CompanyInfoEntity.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}