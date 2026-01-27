package com.sixclassguys.maplecalendar

actual class PermissionChecker {

    actual fun isNotificationGranted(): Boolean {
        // iOS는 권한 체크가 비동기라 실제로는 콜백 처리가 필요하지만,
        // 우선 구조를 맞추기 위해 기본값을 반환하거나 상태를 동기적으로 가져오는 로직이 필요합니다.
        return true // 임시 허용 처리 (추후 구현 가능)
    }
}