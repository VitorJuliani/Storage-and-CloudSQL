package com.example.storageandsqlstudy.service

interface StorageService {

    fun uploadObject(data: ByteArray?): String
    fun updateObject(data: ByteArray?, objectUrl: String): String
    fun deleteObject(objectUrl: String): Boolean
    fun generateSignUrl(objectName: String): String
}
