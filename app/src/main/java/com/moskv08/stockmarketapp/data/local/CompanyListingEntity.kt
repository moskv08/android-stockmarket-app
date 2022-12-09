package com.moskv08.stockmarketapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyListingEntity(
    @PrimaryKey
    val id: Int? = null,
    val symbol: String,
    val name: String,
    val exchange: String,
//    val assetType: String? = null,
//    val ipoDate: Date? = null,
//    val delistingDate: Date? = null,
//    val status: Boolean? = null
)
