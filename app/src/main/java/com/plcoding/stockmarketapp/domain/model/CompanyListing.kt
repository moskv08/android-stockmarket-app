package com.plcoding.stockmarketapp.domain.model

import com.plcoding.stockmarketapp.data.local.CompanyListingEntity

data class CompanyListing(
    val name: String,
    val symbol: String,
    val exchange: String
)
