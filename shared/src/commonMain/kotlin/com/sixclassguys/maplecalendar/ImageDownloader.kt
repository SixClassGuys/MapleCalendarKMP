package com.sixclassguys.maplecalendar

import kotlinx.coroutines.flow.Flow

interface ImageDownloader {

    suspend fun downloadImage(imageUrl: String): Flow<Result<String>>
}