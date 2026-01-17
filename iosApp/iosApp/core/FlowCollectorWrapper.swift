import Foundation
import shared

class FlowCollectorWrapper<T>: NSObject, Kotlinx_coroutines_coreFlowCollector {
    
    private let onEmit: (T?, @escaping (Error?) -> Void) -> Void

    init(emit: @escaping (T?, @escaping (Error?) -> Void) -> Void) {
        self.onEmit = emit
        super.init()
    }

    // 1. CompletionHandler 방식의 메서드 (이름을 다르게 지정)
    @objc(emitValue:completionHandler:)
    func __emit(value: Any?, completionHandler: @escaping @Sendable (Error?) -> Void) {
        onEmit(value as? T, completionHandler)
    }

    // 2. Async 방식의 메서드는 @nonobjc를 붙여 Objective-C 셀렉터 생성을 막음으로써 충돌 해결
    @nonobjc
    func __emit(value: Any?) async throws {
        try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<Void, Error>) in
            self.__emit(value: value) { error in
                if let error = error {
                    continuation.resume(throwing: error)
                } else {
                    continuation.resume()
                }
            }
        }
    }
}
