package com.example.storageandsqlstudy.model

import java.time.LocalDate

data class BookRequest(
        val name: String,
        val author: String,
        val price: Long,
        val creationDate: LocalDate,
        val newImage: String?,
        val currentImage: String?
)
