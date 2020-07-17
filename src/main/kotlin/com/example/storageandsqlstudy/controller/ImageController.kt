package com.example.storageandsqlstudy.controller

import com.example.storageandsqlstudy.service.StorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/image")
class ImageController(private val storageService: StorageService) {

    @GetMapping("/{objectName}")
    fun getSignUrl(@PathVariable("objectName") objectName: UUID): ResponseEntity<String> {
        val signUrl = storageService.generateSignUrl(objectName.toString())

        return ResponseEntity.ok(signUrl)
    }
}
