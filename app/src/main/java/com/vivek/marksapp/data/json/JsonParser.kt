package com.vivek.marksapp.data.json

interface JsonParser<T> {
    suspend fun parse(): List<T>
}