package com.example.storageandsqlstudy.service

import com.example.storageandsqlstudy.model.Book
import com.example.storageandsqlstudy.model.BookRequest
import com.example.storageandsqlstudy.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.Exception

@Service
class BookServiceImp(private val bookRepository: BookRepository,
                     private val storageService: StorageService) : BookService {

    override fun saveBook(bookRequest: BookRequest): Book {
        val imageAsByteArray = Base64.getDecoder().decode(bookRequest.newImage)

        val imageUrl = storageService.uploadObject(imageAsByteArray)

        val book = Book(bookRequest, imageUrl)

        return bookRepository.save(book)
    }

    override fun getBookById(id: Long): Book {
        return bookRepository.getOne(id)
    }

    override fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    override fun updateBook(id: Long, bookRequest: BookRequest): Book {
        val imageAsByteArray = Base64.getDecoder().decode(bookRequest.newImage)

        val imageUrl: String = bookRequest.currentImage?.let {
            storageService.updateObject(imageAsByteArray, it)
        } ?: throw Exception("Current image is necessary to update")

        val book = Book(bookRequest, imageUrl)

        val updateResultAffectedRows = bookRepository.updateBook(id, book)

        if (updateResultAffectedRows == 0) {
            throw Exception("Error to update book")
        }

        return bookRepository.getOne(id)
    }

    override fun deleteBook(id: Long) {
        val bookToBeDeleted = bookRepository.getOne(id)

        val isBookImageDeleted = storageService.deleteObject(bookToBeDeleted.image)

        if (isBookImageDeleted) {
            bookRepository.deleteById(id)
        } else {
            throw Exception("Image not found")
        }
    }
}
