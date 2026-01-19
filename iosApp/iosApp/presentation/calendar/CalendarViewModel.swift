import Foundation
import shared

@MainActor
class CalendarViewModel: ObservableObject {
    
    @Published var uiState = CalendarUiState(
        isLoading: false,
        nexonApiKey: nil,
        isGlobalAlarmEnabled: false,
        isRefreshing: false,
        year: 0,
        month: Kotlinx_datetimeMonth.january,
        days: [],
        eventsMapByDay: [:],
        eventsMapByMonth: [:],
        selectedDate: nil,
        selectedEvent: nil,
        isNotificationEnabled: false,
        showAlarmDialog: false,
        scheduledNotifications: [],
        showBottomSheet: false,
        errorMessage: nil
    )
    
    private lazy var helper = KMPHelperKt.getKMPHelper()
    
    private lazy var getApiKeyUseCase = helper.getApiKeyUseCase
    private lazy var getGlobalAlarmStatusUseCase = helper.getGlobalAlarmStatusUseCase
    private lazy var getTodayEventsUseCase = helper.getTodayEventsUseCase
    private lazy var getMonthlyEventsUseCase = helper.getMonthlyEventsUseCase
    private lazy var getEventDetailUseCase = helper.getEventDetailUseCase
    private lazy var submitEventAlarmUseCase = helper.submitEventAlarmUseCase
    private lazy var toggleEventAlarmUseCase = helper.toggleEventAlarmUseCase
    private lazy var reducer = helper.calendarReducer
    private lazy var eventBus = helper.notificationEventBus
    
    init() {
        onIntent(intent: CalendarIntent.FetchNexonOpenApiKey())
        onIntent(intent: CalendarIntent.FetchGlobalAlarmStatus())
        observeEventBus()
    }
    
    private func observeEventBus() {
        Task {
            // SKIE 덕분에 collect 대신 'for await'를 사용하여
            // 시퀀스처럼 이벤트를 하나씩 받아올 수 있다.
            for await eventId in eventBus.events {
                // eventId가 KotlinLong(Int64) 타입으로 들어옴
                // onIntent의 SelectEvent는 Int64를 받으므로 바로 전달하거나 변환
                let id = eventId.int64Value
                self.onIntent(intent: CalendarIntent.SelectEvent(eventId: id))
            }
        }
    }
    
    private func getNexonOpenApiKey() {
        Task {
            do {
                let flow = try await getApiKeyUseCase.invoke()
                    
                try await flow.collect(collector: FlowCollectorWrapper<AnyObject> { state, completionHandler in
                    Task { @MainActor in
                        if let success = state as? ApiStateSuccess<NSString>,
                               let key = success.data as String? {
                                self.onIntent(intent: CalendarIntent.FetchNexonOpenApiKeySuccess(key: key))
                        } else if let error = state as? ApiStateError {
                                self.onIntent(intent: CalendarIntent.FetchNexonOpenApiKeyFailed(message: error.message))
                        }
                        completionHandler(nil)
                    }
                })
            } catch {
                print("API Key Load Error: \(error)")
            }
        }
    }
    
    private func getGlobalAlarmStatus() {
        Task {
            do {
                let flow = try await getGlobalAlarmStatusUseCase.invoke()
                
                try await flow.collect(collector: FlowCollectorWrapper<AnyObject> { state, completionHandler in
                    Task { @MainActor in
                        if let success = state as? ApiStateSuccess<AnyObject> {
                            let isEnabled: Bool
                            if let boolNum = success.data as? NSNumber {
                                isEnabled = boolNum.boolValue
                                print("Fetched Alarm Status as NSNumber: \(isEnabled)") // 로그로 확인
                            } else {
                                isEnabled = success.data as? Bool ?? false
                                print("Fetched Alarm Status as Bool: \(isEnabled)") // 로그로 확인
                            }
                            self.onIntent(intent: CalendarIntent.FetchGlobalAlarmStatusSuccess(isEnabled: isEnabled))
                        } else if let error = state as? ApiStateError {
                            self.onIntent(intent: CalendarIntent.FetchGlobalAlarmStatusFailed(message: error.message))
                        }
                        completionHandler(nil)
                    }
                })
            } catch {
                print("Global Alarm Status Load Error: \(error)")
            }
        }
    }
    
    private func fetchEventsByDay(year: Int32, month: Int32, day: Int32, key: String) {
        Task {
            do {
                print("Fetching for key: \(key)")
                let apiKey = uiState.nexonApiKey ?? ""
                let flow = try await getTodayEventsUseCase.invoke(year: year, month: month, day: day, apiKey: apiKey)
                
                // flow.collect 내부의 클로저가 어느 스레드에서 실행될지 알 수 없으므로
                // 내부에서 다시 MainActor를 명시합니다.
                try await flow.collect(collector: FlowCollectorWrapper<AnyObject> { state, completionHandler in
                    Task { @MainActor in
                        if let apiState = state as? ApiState<NSArray> {
                            self.onIntent(intent: CalendarIntent.SaveEventsByDay(key: key, apiState: apiState))
                        }
                        completionHandler(nil)
                    }
                })
            } catch {
                print("Day fetch error: \(error)")
            }
        }
    }
        
    private func fetchEventsByMonth(year: Int32, month: Int32, key: String) {
        Task {
            do {
                // 1. SKIE로 인해 invoke() 결과가 AsyncSequence를 지원하는 SkieSwiftFlow로 옵니다.
                let flow = try await getMonthlyEventsUseCase.invoke(year: year, month: month)
                
                // 2. collect 대신 for await 사용
                for await state in flow {
                    // state는 ApiState<NSArray> 타입으로 들어옵니다.
                    if let apiState = state as? ApiState<NSArray> {
                        // 3. 인텐트에 그대로 전달 (이미 NSArray 타입이므로 추가 캐스팅 없이 깔끔하게 전달 가능합니다)
                        self.onIntent(intent: CalendarIntent.SaveEventsByMonth(key: key, apiState: apiState))
                    }
                }
            } catch {
                print("월별 이벤트 로드 실패: \(error)")
            }
        }
    }
        
    private func fetchEventDetail(eventId: Int64) {
        Task {
            let apiKey = uiState.nexonApiKey ?? ""
            let flow = try await getEventDetailUseCase.invoke(apiKey: apiKey, eventId: eventId)
            try await flow.collect(collector: FlowCollectorWrapper<AnyObject> { state, _ in
                if let success = state as? ApiStateSuccess<MapleEvent> {
                    self.onIntent(intent: CalendarIntent.SelectEventSuccess(event: success.data))
                } else if let error = state as? ApiStateError {
                    self.onIntent(intent: CalendarIntent.SelectEventFailed(message: error.message))
                }
            })
        }
    }
        
    private func toggleEventAlarm() {
        Task {
            let apiKey = uiState.nexonApiKey ?? ""
            let eventId = uiState.selectedEvent?.id ?? 0
            let flow = try await toggleEventAlarmUseCase.invoke(apiKey: apiKey, eventId: eventId)
            try await flow.collect(collector: FlowCollectorWrapper<AnyObject> { state, _ in
                if let success = state as? ApiStateSuccess<MapleEvent> {
                    self.onIntent(intent: CalendarIntent.ToggleNotificationSuccess(event: success.data!))
                }
            })
        }
    }
    
    func isDateSelected(_ date: Kotlinx_datetimeLocalDate) -> Bool {
            return uiState.selectedDate == date
    }
        
    func isToday(_ date: Kotlinx_datetimeLocalDate) -> Bool {
        return date == reducer.getTodayDate()
    }
        
    func getSelectedDateEvents() -> [MapleEvent] {
        guard let date = uiState.selectedDate else { return [] }
        let key = "\(date.year)-\(date.monthNumber)-\(date.dayOfMonth)"
        return uiState.eventsMapByDay[key] ?? []
    }
    
    @MainActor
    func onIntent(intent: CalendarIntent) {
        self.uiState = reducer.reduce(currentState: self.uiState, intent: intent)
        
        switch intent {
            case is CalendarIntent.Refresh:
                let date = uiState.selectedDate ?? reducer.getTodayDate()
                onIntent(intent: CalendarIntent.SelectDate(date: date))
            case is CalendarIntent.FetchNexonOpenApiKey:
                getNexonOpenApiKey()
            case is CalendarIntent.FetchGlobalAlarmStatus:
                getGlobalAlarmStatus()
            case is CalendarIntent.FetchNexonOpenApiKeySuccess:
                onIntent(intent: CalendarIntent.ChangeMonth(offset: 0))
                let today = reducer.getTodayDate()
                onIntent(intent: CalendarIntent.SelectDate(date: today))
            case let changeMonth as CalendarIntent.ChangeMonth:
                let targetDate = reducer.getLocalDateByOffset(offset: changeMonth.offset)
                let monthKey = "\(targetDate.year)-\(targetDate.monthNumber)"
                let dayKey = "\(targetDate.year)-\(targetDate.monthNumber)-\(targetDate.dayOfMonth)"
                if uiState.eventsMapByDay[dayKey] == nil {
                    fetchEventsByDay(year: targetDate.year, month: targetDate.monthNumber, day: targetDate.dayOfMonth, key: dayKey)
                }
                if uiState.eventsMapByMonth[monthKey] == nil {
                    fetchEventsByMonth(year: targetDate.year, month: targetDate.monthNumber, key: monthKey)
                }
                    
            case let selectDate as CalendarIntent.SelectDate:
                let date = selectDate.date
                let key = "\(date.year)-\(date.monthNumber)-\(date.dayOfMonth)"
                if uiState.eventsMapByDay[key] == nil {
                    fetchEventsByDay(year: date.year, month: date.monthNumber, day: date.dayOfMonth, key: key)
                }
            case let selectEvent as CalendarIntent.SelectEvent:
                    fetchEventDetail(eventId: selectEvent.eventId)
            case is CalendarIntent.ToggleNotification:
                    toggleEventAlarm()
            default:
                break
        }
    }
}
