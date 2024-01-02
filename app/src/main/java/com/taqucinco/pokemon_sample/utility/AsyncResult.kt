package com.taqucinco.pokemon_sample.utility

sealed class AsyncResult<out T> {
    data class Success<out T>(val data: T) : AsyncResult<T>()
    data class Error(val error: Throwable) : AsyncResult<Nothing>()
    object Loading: AsyncResult<Nothing>()
}
