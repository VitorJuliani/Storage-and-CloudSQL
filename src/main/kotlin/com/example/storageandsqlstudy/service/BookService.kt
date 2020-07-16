package com.example.storageandsqlstudy.service

import com.example.storageandsqlstudy.model.Book
import com.example.storageandsqlstudy.model.BookRequest

interface BookService {

    fun saveBook(bookRequest: BookRequest): Book
    fun getBookById(id: Long): Book
    fun getAllBooks(): List<Book>
    fun updateBook(id: Long, bookRequest: BookRequest): Book
    fun deleteBook(id: Long)
}
