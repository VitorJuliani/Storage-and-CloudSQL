package com.example.storageandsqlstudy.controller

import com.example.storageandsqlstudy.model.Book
import com.example.storageandsqlstudy.model.BookRequest
import com.example.storageandsqlstudy.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {

    @GetMapping("/{id}")
    fun getBook(@PathVariable("id") id: Long): ResponseEntity<Book> {
        val book = bookService.getBookById(id)
        return ResponseEntity.ok(book)
    }

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<Book>> {
        val books = bookService.getAllBooks()
        return ResponseEntity.ok(books)
    }

    @PostMapping
    fun saveBook(@RequestBody book: BookRequest): ResponseEntity<Book> {
        val savedBook = bookService.saveBook(book)

        val location = ServletUriComponentsBuilder
                .fromPath("/book/${savedBook.id}")
                .build()
                .toUri()

        return ResponseEntity.created(location).body(savedBook)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable("id") id: Long, @RequestBody book: BookRequest): ResponseEntity<Book> {
        val updatedBook = bookService.updateBook(id, book)
        return ResponseEntity.ok(updatedBook)
    }

    @DeleteMapping("/{id}")
    fun updateBook(@PathVariable("id") id: Long): ResponseEntity<Any> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}
