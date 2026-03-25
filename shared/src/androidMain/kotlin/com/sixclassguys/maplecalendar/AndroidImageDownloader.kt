package com.sixclassguys.maplecalendar

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AndroidImageDownloader(
    private val context: Context
) : ImageDownloader {

    override suspend fun downloadImage(imageUrl: String): Flow<Result<String>> = flow {
        try {
            Napier.d("🚀 [1] Flow 시작 - URL: $imageUrl")
            emit(Result.success("LOADING_STARTED"))

            val result = withContext(Dispatchers.IO) {
                Napier.d("🚀 [2] 비트맵 다운로드 시도 중...")
                val bitmap = downloadBitmap(imageUrl)

                if (bitmap == null) {
                    Napier.e("❌ [Error] 비트맵이 null입니다. URL을 확인하세요.")
                    return@withContext "이미지를 불러오지 못했습니다."
                }

                Napier.d("🚀 [3] 비트맵 다운로드 성공. MediaStore 저장 시작...")
                saveBitmapToMediaStore(context, bitmap, "MapleBgm_${System.currentTimeMillis()}")
            }

            Napier.d("🚀 [4] 최종 결과: $result")
            if (result.contains("성공") || result.contains("저장되었어요")) {
                emit(Result.success(result))
            } else {
                emit(Result.failure(Exception(result)))
            }
        } catch (e: Exception) {
            Napier.e("🔥 [Fatal Error] 예상치 못한 예외 발생: ${e.message}")
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    // 1. URL로부터 Bitmap 다운로드 (Pure Kotlin)
    private fun downloadBitmap(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 2. Bitmap을 MediaStore(갤러리)에 저장 (Scoped Storage 대응)
    private fun saveBitmapToMediaStore(context: Context, bitmap: Bitmap, fileName: String): String {
        val resolver = context.contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val now = System.currentTimeMillis() / 1000
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, now)
            put(MediaStore.Images.Media.DATE_TAKEN, now)
            // Android Q 이상에서는 상대 경로 설정 가능 (Download 폴더 내 하위 폴더)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Maplendar")
                put(MediaStore.Images.Media.IS_PENDING, 1) // 저장 중임을 표시
            }
        }

        val imageUri: Uri? = resolver.insert(imageCollection, imageDetails)

        return if (imageUri != null) {
            try {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageDetails.clear()
                    imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장 완료
                    resolver.update(imageUri, imageDetails, null, null)
                }
                "이미지가 Download 폴더에 저장되었어요."
            } catch (e: Exception) {
                resolver.delete(imageUri, null, null) // 실패 시 레코드 삭제
                "저장 실패: ${e.message}"
            }
        } else {
            "미디어 스토어 생성 실패"
        }
    }
}