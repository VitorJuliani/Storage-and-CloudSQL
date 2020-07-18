package com.example.storageandsqlstudy.service

import com.example.storageandsqlstudy.model.Book
import com.example.storageandsqlstudy.model.BookRequest
import com.example.storageandsqlstudy.property.GoogleCloudStorageProperties
import com.example.storageandsqlstudy.repository.BookRepository
import com.example.storageandsqlstudy.repository.ImageGoogleCloudStorageDao
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*
import kotlin.Exception

@Service
class BookService(private val bookRepository: BookRepository,
                  private val storageDao: ImageGoogleCloudStorageDao,
                  private val googleCloudStorageProperties: GoogleCloudStorageProperties) {

    fun saveBook(bookRequest: BookRequest): Book {

        val imageUrl = bookRequest.newImage?.let {
            Base64.getDecoder().decode(bookRequest.newImage)
        }?.let { imageAsByteArray ->
            storageDao.uploadObject(imageAsByteArray)
        }

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

        if (bookTobeUpdated.image != bookRequest.currentImage) {
            throw Exception("Invalid image")
        }

        val imageAsByteArray: ByteArray? = bookRequest.newImage?.let {
            Base64.getDecoder().decode(it)
        }

        val imageUrl = bookRequest.currentImage?.let {
            URL(it).path
                    .removePrefix("/${googleCloudStorageProperties.baseUrl}/")
                    .removeSuffix(".${googleCloudStorageProperties.image.imageExtension}")
        }?.let {
            if (imageAsByteArray != null) {
                storageDao.updateObject(imageAsByteArray, it)
            } else {
                storageDao.deleteObject(it)
                null
            }
        } ?: storageDao.uploadObject(imageAsByteArray)

        val book = Book(bookRequest, imageUrl)

        val updateResultAffectedRows = bookRepository.updateBook(id, book)

        if (updateResultAffectedRows == 0) {
            throw Exception("Error to update book")
        }

        return bookRepository.getOne(id)
    }

    fun deleteBook(id: Long) {
        val bookToBeDeleted = bookRepository.getOne(id)

        val isBookImageDeleted = bookToBeDeleted.image?.let {
            storageDao.deleteObject(it)
        } ?: true

        if (isBookImageDeleted) {
            bookRepository.deleteById(id)
        } else {
            throw Exception("Image not found")
        }
    }
}
