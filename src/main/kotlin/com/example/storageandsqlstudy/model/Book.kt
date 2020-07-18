package com.example.storageandsqlstudy.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "book")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,
    val name: String,
    val author: String,
    val price: Long,
    val creationDate: LocalDate,
    val image: String?
) {
    constructor(bookRequest: BookRequest, image: String?) : this(
            null,
            bookRequest.name,
            bookRequest.author,
            bookRequest.price,
            bookRequest.creationDate,
            image
    )
}
