package com.example.storageandsqlstudy.repository

import com.example.storageandsqlstudy.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface BookRepository : JpaRepository<Book, Long> {

    @Transactional
    @Modifying
    @Query("update Book book SET book.name = :#{#book.name}, book.author = :#{#book.author}, book.price = :#{#book.price}, book.creationDate = :#{#book.creationDate}, book.image = :#{#book.image} WHERE book.id=:bookId")
    fun updateBook(@Param("bookId") id: Long, @Param("book") book: Book): Int
}
