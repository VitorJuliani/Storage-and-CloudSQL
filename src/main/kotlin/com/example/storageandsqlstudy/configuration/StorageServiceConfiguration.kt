package com.example.storageandsqlstudy.configuration

import com.example.storageandsqlstudy.property.GoogleCloudStorageProperties
import com.example.storageandsqlstudy.service.ImageGoogleCloudStorageService
import com.example.storageandsqlstudy.service.StorageService
import com.google.cloud.storage.Storage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageServiceConfiguration {

    @Bean
    fun imageGoogleCloudStorageService(storage: Storage,
                                       storageProperties: GoogleCloudStorageProperties): StorageService {
        return ImageGoogleCloudStorageService(storage, storageProperties)
    }
}
