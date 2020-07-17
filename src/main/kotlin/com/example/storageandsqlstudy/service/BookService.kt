package com.example.storageandsqlstudy.service

import com.example.storageandsqlstudy.model.Book
import com.example.storageandsqlstudy.model.BookRequest
import com.example.storageandsqlstudy.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.Exception

@Service
class BookService(private val bookRepository: BookRepository,
                  private val storageService: ImageGoogleCloudStorageService) {

    fun saveBook(bookRequest: BookRequest): Book {
        val imageAsByteArray = Base64.getDecoder().decode(bookRequest.newImage)

        val imageUrl = storageService.uploadObject(imageAsByteArray)

        val book = Book(bookRequest, imageUrl)

        return bookRepository.save(book)
    }

    fun getBookById(id: Long): Book {
        return bookRepository.getOne(id)
    }

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    fun updateBook(id: Long, bookRequest: BookRequest): Book {
        val bookTobeUpdated = bookRepository.getOne(id)

        if (bookRequest.currentImage == null || bookTobeUpdated.image != bookRequest.currentImage) {
            throw Exception("Insert a valid image")
        }

        val imageAsByteArray = bookRequest.newImage?.let {
            Base64.getDecoder().decode(it)
        }

        val imageUrl: String = storageService.updateObject(imageAsByteArray, bookRequest.currentImage)

        val book = Book(bookRequest, imageUrl)

        val updateResultAffectedRows = bookRepository.updateBook(id, book)

        if (updateResultAffectedRows == 0) {
            throw Exception("Error to update book")
        }

        return bookRepository.getOne(id)
    }

    fun deleteBook(id: Long) {
        val bookToBeDeleted = bookRepository.getOne(id)

        val isBookImageDeleted = storageService.deleteObject(bookToBeDeleted.image)

        if (isBookImageDeleted) {
            bookRepository.deleteById(id)
        } else {
            throw Exception("Image not found")
        }
    }
}
