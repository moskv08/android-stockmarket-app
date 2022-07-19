package com.plcoding.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private var repository: StockRepository
) : ViewModel() {

    private var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null

    fun onEvent(event: CompanyListingEvents){
        when(event) {
            is CompanyListingEvents.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvents.onSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        fetchFromRemote: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ){
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote, query)
                .collect { result ->
                    when(result){
                        is Resource.Success -> {
                            result.data?.let { listingOfCompanies ->
                                state = state.copy(companies = listingOfCompanies)
                            }

                        }
                        is Resource.Error -> Unit
                            // TODO: Implement Error Handling!
                        is Resource.isLoading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}