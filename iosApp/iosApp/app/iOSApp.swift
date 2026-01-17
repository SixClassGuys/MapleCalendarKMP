import SwiftUI
import shared

@main
struct iOSApp: App {
    // 1. 선언만 하고 초기화는 init에서 수행
    @StateObject private var homeViewModel: HomeViewModel

    init() {
        // 2. Koin 초기화를 최우선으로 실행
        // KoinModuleKt.doInitKoin(additionalModules: [], appDeclaration: { _ in })
        KoinIosKt.doInitKoinIos()
        
        
        // 3. 초기화가 끝난 후 ViewModel 인스턴스 생성
        _homeViewModel = StateObject(wrappedValue: HomeViewModel())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
