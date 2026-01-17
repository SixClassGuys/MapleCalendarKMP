import SwiftUI

extension Color {
    // Hex 문자열로 Color를 생성하는 초기화자
    init(hex: String) {
        let scanner = Scanner(string: hex)
        _ = scanner.scanString("#")
        var rgb: UInt64 = 0
        scanner.scanHexInt64(&rgb)
        let r = Double((rgb >> 16) & 0xFF) / 255.0
        let g = Double((rgb >>  8) & 0xFF) / 255.0
        let b = Double((rgb >>  0) & 0xFF) / 255.0
        self.init(red: r, green: g, blue: b)
    }

    // 💡 앱에서 자주 쓰는 전역 색상을 미리 정의해두면 편리합니다.
    static let mapleOrange = Color(hex: "ED962A")
    static let mapleWhite = Color(hex: "FFFFFF")
    static let mapleGray = Color(hex: "9F9F9F")
    static let mapleBlack = Color(hex: "000000")
    static let mapleStatBackground = Color(hex: "343A41")
    static let mapleStatTitle = Color(hex: "DCF44F")
}
