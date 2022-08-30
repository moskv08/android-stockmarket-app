package com.moskv08.stockmarketapp.util

sealed class Resource<T> (val data: T? = null, val message: String? = null){
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class isLoading<T>(val isLoading: Boolean = false): Resource<T>()
//    class isLoading<T>(val isLoading: Boolean = false): Resource<T>(null)
}
