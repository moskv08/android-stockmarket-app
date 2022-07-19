package com.plcoding.stockmarketapp.presentation.company_listings

// Handling different UI events that can performed on a single screen
sealed class CompanyListingEvents{
    object Refresh: CompanyListingEvents()
    data class onSearchQueryChange(val query: String): CompanyListingEvents()
}
