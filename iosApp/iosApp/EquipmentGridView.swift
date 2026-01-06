//
//  EquipmentGridView.swift
//  iosApp
//
//  Created by 이상혁 on 1/6/26.
//

import SwiftUI
struct EquipmentGridView: View {
    let columnCount = 4
    let itemCount = 23   // 실제 아이템 개수

    let columns = Array(
        repeating: GridItem(.flexible(), spacing: 8),
        count: 5
    )

    var body: some View {
        LazyVGrid(columns: columns, spacing: 8) {
            ForEach(gridSlots.indices, id: \.self) { slotIndex in
                if let itemIndex = gridSlots[slotIndex] {
                    EquipmentItemView(index: itemIndex + 1)
                } else {
                    Color.clear
                        .frame(height: 70)
                }
            }
        }
    }

    /// Grid 슬롯 정의
    var gridSlots: [Int?] {
        var slots: [Int?] = []
        var currentItemIndex = 0
        var row = 0

        while currentItemIndex < itemCount {
            for column in 0..<columnCount {

                // 첫 줄 or 마지막 줄의 3번째 칸 비우기
                if (row == 0 && column == 2) {
                    slots.append(nil)
                    continue
                }

                // 마지막 줄 판단은 "아이템이 거의 끝났을 때"
                let remainingItems = itemCount - currentItemIndex
                if remainingItems <= columnCount &&
                   column == 2 {
                    slots.append(nil)
                    continue
                }

                if currentItemIndex < itemCount {
                    slots.append(currentItemIndex)
                    currentItemIndex += 1
                }
            }
            row += 1
        }

        return slots
    }

}
struct EquipmentItemView: View {
    let index: Int

    var body: some View {
        ZStack {
            Rectangle()
                .fill(Color(.darkGray))
                .overlay(
                    Rectangle().stroke(Color.yellow, lineWidth: 1)
                )

            Text("\(index)")
                .foregroundColor(.white)
                .bold()
        }
        .frame(height: 70)
    }
}




