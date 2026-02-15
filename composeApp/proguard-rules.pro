# ---------------------------------------------------------
# 1. 공통 필수 규칙 (Kotlin & Coroutines)
# ---------------------------------------------------------
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keep class kotlin.coroutines.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }
-keep class androidx.lifecycle.ViewModel { *; }
-keep class androidx.lifecycle.ViewModelKt { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# ---------------------------------------------------------
# 2. Kotlinx Serialization (JSON 파싱용 DTO 보호)
# 매우 중요: 이 부분을 설정하지 않으면 서버 데이터 파싱 시 에러가 납니다.
# ---------------------------------------------------------
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
# 본인의 DTO 패키지 경로 (실제 경로에 맞춰 수정하세요)
-keep class com.sixclassguys.maplecalendar.data.model.** { *; }
-keep class com.sixclassguys.maplecalendar.remote.dto.** { *; }

# ---------------------------------------------------------
# 3. Ktor (Network)
# ---------------------------------------------------------
-keep class io.ktor.** { *; }
-keep class okhttp3.** { *; }
-dontwarn io.ktor.**
-dontwarn okhttp3.**

# ---------------------------------------------------------
# 4. Koin (Dependency Injection)
# ---------------------------------------------------------
-keep class io.insert_koin.** { *; }
-dontwarn io.insert_koin.**

# ---------------------------------------------------------
# 5. Coil 3 (Image Loading)
# ---------------------------------------------------------
-keep class io.coil_kt.coil3.** { *; }
-dontwarn io.coil_kt.coil3.**

# ---------------------------------------------------------
# 6. Compose & Navigation
# ---------------------------------------------------------
-keep class androidx.compose.** { *; }
-keep class androidx.navigation.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }
-dontwarn androidx.compose.**

# ---------------------------------------------------------
# 7. Google Identity / Google ID
# ---------------------------------------------------------
-keep class com.google.android.libraries.identity.googleid.** { *; }