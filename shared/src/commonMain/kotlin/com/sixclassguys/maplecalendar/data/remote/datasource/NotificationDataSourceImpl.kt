package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.TokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotificationDataSourceImpl(
    private val httpClient: HttpClient
) : NotificationDataSource {

    override suspend fun registerToken(request: TokenRequest): HttpResponse {
        // 백엔드 주소는 추후 상수로 관리하기
        return httpClient.post("http://10.0.2.2:8080/api/v1/notifications/tokens") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}