package com.moskv08.stockmarketapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.sql.Date

@Entity
data class CompanyInfoEntity(
    @PrimaryKey
    val id: Int? = null,
    val symbol: String?,
    val description: String?,
    val name: String?,
    val country: String?,
    val industry: String?,
)
