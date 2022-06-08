package com.vivek.marksapp.data.remote.model

data class OptionDto(
    val id: String,

    val image: String? = null,

    val isCorrect: Boolean,

    val text: String
)