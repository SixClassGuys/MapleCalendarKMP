//
//  CharacterCard.swift
//  iosApp
//
//  Created by 이상혁 on 1/6/26.
//

import SwiftUI
//
//  CharacterCardView.swift
//  iosApp
//
//  Created by 이상혁 on 1/6/26.
//


struct CharacterCardView: View {
    var body: some View {
        HStack(spacing: 16) {
            Image("character") // Assets에 캐릭터 추가
                .resizable()
                .frame(width: 60, height: 60)

            VStack(alignment: .leading, spacing: 6) {
                Text("과당렌")
                    .foregroundColor(.white)
                    .font(.headline)

                Text("Lv.286 | 유니온 8,855")
                    .foregroundColor(.gray)
                    .font(.subheadline)

                Text("전투력 152,208,556")
                    .foregroundColor(.yellow)
                    .font(.subheadline)
            }

            Spacer()
        }
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(Color(.darkGray))
        )
    }
}
