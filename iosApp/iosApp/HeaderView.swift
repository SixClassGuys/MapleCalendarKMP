//
//  HeaderView.swift
//  iosApp
//
//  Created by 이상혁 on 1/4/26.
//

import SwiftUI

struct HeaderView: View {
    var body: some View {
        HStack {
            Image(systemName: "leaf").foregroundColor(.white)
            Text("메이플 캘린더")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.white)

            Spacer()

            HStack(spacing: 16) {
                Image(systemName: "line.3.horizontal")
                Image(systemName: "star")
                Image(systemName: "magnifyingglass")
            }
            .foregroundColor(.white)
        }
        .padding(.top, 8)
    }
}
