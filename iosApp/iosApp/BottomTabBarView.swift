//
//  BottomTabBarView.swift
//  iosApp
//
//  Created by 이상혁 on 1/6/26.
//

import SwiftUI

struct BottomTabBarView: View {
    var body: some View {
        HStack {
            Spacer()
            BottomTabItem(icon: "house", title: "홈")
            Spacer()
            BottomTabItem(icon: "doc.text", title: "피드")
            Spacer()
            BottomTabItem(icon: "crown", title: "랭킹")
            Spacer()
            BottomTabItem(icon: "square.grid.2x2", title: "전체")
            Spacer()
        }
        .padding()
        .background(Color(.darkGray))
        .cornerRadius(16)
    }
}

struct BottomTabItem: View {
    let icon: String
    let title: String

    var body: some View {
        VStack {
            Image(systemName: icon)
            Text(title)
                .font(.caption)
        }
        .foregroundColor(.white)
    }
}

