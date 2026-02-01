package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyChatMessageRequest
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyCreateRequest
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyCreateResponse
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyDetailResponse
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyResponse
import com.sixclassguys.maplecalendar.util.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

class BossDataSourceImpl(
    private val httpClient: HttpClient
) : BossDataSource {

    private var session: DefaultClientWebSocketSession? = null

    override suspend fun getBossParties(accessToken: String): List<BossPartyResponse> {
        val response = try {
            httpClient.get("boss-parties") {
                header("Authorization", "Bearer $accessToken")
            }
        } catch (e: Exception) {
            // 아예 서버에 접속조차 못하는 상황 (인터넷 끊김 등)
            throw ApiException(message = "$e: 인터넷 연결을 확인해주세요.")
        }

        return when (response.status.value) {
            in 200..299 -> {
                response.body()
            }

            400 -> throw ApiException(400, "잘못된 요청입니다. 입력값을 확인해주세요.")
            401 -> throw ApiException(401, "인증 정보가 만료되었습니다. 다시 로그인해주세요.")
            404 -> throw ApiException(404, "요청하신 이벤트 데이터를 찾을 수 없습니다.")
            in 500..599 -> {
                throw ApiException(response.status.value, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
            }

            else -> {
                throw ApiException(
                    response.status.value,
                    "알 수 없는 오류가 발생했습니다. (Code: ${response.status.value})"
                )
            }
        }
    }

    override suspend fun createBossParty(
        accessToken: String,
        request: BossPartyCreateRequest
    ): BossPartyCreateResponse {
        val response = try {
            httpClient.post("boss-parties") {
                header("Authorization", "Bearer $accessToken")
                setBody(request)

                contentType(ContentType.Application.Json)
            }
        } catch (e: Exception) {
            // 아예 서버에 접속조차 못하는 상황 (인터넷 끊김 등)
            throw ApiException(message = "$e: 인터넷 연결을 확인해주세요.")
        }

        return when (response.status.value) {
            in 200..299 -> {
                response.body()
            }

            400 -> throw ApiException(400, "잘못된 요청입니다. 입력값을 확인해주세요.")
            401 -> throw ApiException(401, "인증 정보가 만료되었습니다. 다시 로그인해주세요.")
            404 -> throw ApiException(404, "요청하신 이벤트 데이터를 찾을 수 없습니다.")
            in 500..599 -> {
                throw ApiException(response.status.value, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
            }

            else -> {
                throw ApiException(
                    response.status.value,
                    "알 수 없는 오류가 발생했습니다. (Code: ${response.status.value})"
                )
            }
        }
    }

    override suspend fun getBossPartyDetail(
        accessToken: String,
        bossPartyId: Long
    ): BossPartyDetailResponse {
        val response = try {
            httpClient.get("boss-parties/$bossPartyId") {
                header("Authorization", "Bearer $accessToken")
            }
        } catch (e: Exception) {
            // 아예 서버에 접속조차 못하는 상황 (인터넷 끊김 등)
            throw ApiException(message = "$e: 인터넷 연결을 확인해주세요.")
        }

        return when (response.status.value) {
            in 200..299 -> {
                response.body()
            }

            400 -> throw ApiException(400, "잘못된 요청입니다. 입력값을 확인해주세요.")
            401 -> throw ApiException(401, "인증 정보가 만료되었습니다. 다시 로그인해주세요.")
            404 -> throw ApiException(404, "요청하신 이벤트 데이터를 찾을 수 없습니다.")
            in 500..599 -> {
                throw ApiException(response.status.value, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
            }

            else -> {
                throw ApiException(
                    response.status.value,
                    "알 수 없는 오류가 발생했습니다. (Code: ${response.status.value})"
                )
            }
        }
    }

    override suspend fun connect(partyId: String, token: String) {
        // 이미 연결되어 있고 세션이 활성화 상태라면 재연결 안 함
        if (session?.isActive == true) return

        try {
            session = httpClient.webSocketSession {
                // 1. ws 프로토콜 사용 확인
                url("ws://192.168.0.14:8080/ws-chat?partyId=$partyId&token=$token")
            }
        } catch (e: Exception) {
            session = null // 실패 시 세션 초기화
            throw ApiException(message = "채팅 서버에 연결할 수 없습니다: ${e.message}")
        }
    }

    override suspend fun sendMessage(request: BossPartyChatMessageRequest) {
        val currentSession = session
        if (currentSession == null || !currentSession.isActive) {
            throw ApiException(message = "서버와 연결이 끊어져 있습니다.")
        }

        try {
            currentSession.sendSerialized(request)
        } catch (e: Exception) {
            throw ApiException(message = "메시지 전송 실패: ${e.message}")
        }
    }

    override suspend fun deleteMessage(messageId: Long, token: String) {
        try {
            // 2. 정확한 API 경로 확인 (api/chat)
            httpClient.delete("api/chat/$messageId") {
                header("Authorization", "Bearer $token")
            }
        } catch (e: Exception) {
            throw ApiException(message = "메시지 삭제 요청 실패")
        }
    }

    override fun observeMessages(): Flow<Frame> {
        // session이 null이 될 경우를 대비해 consumeAsFlow 보다는 receiveAsFlow 권장
        // 하지만 세션 재생성 시 Flow가 끊기는 문제는 Repository/ViewModel에서 재구독 로직으로 해결해야 함
        return session?.incoming?.receiveAsFlow() ?: emptyFlow()
    }

    override suspend fun disconnect() {
        try {
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "User disconnected"))
        } catch (e: Exception) {
            // 무시
        } finally {
            session = null
        }
    }
}