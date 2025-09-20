//
//  InfoBubble.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Swift

struct InfoBubble: View {
    let text: String
    let onDismiss: () -> Void
    
    var body: some View {
        VStack {
            Text(text)
                .font(.custom("ReadexPro-Light", size: 12))
                .foregroundColor(.black)
                .multilineTextAlignment(.leading)
                .lineLimit(4)
                .padding(.horizontal, 12)
                .padding(.vertical, 8)
                .frame(maxWidth: 200)
                .background(Color.white)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                )
                .clipShape(RoundedRectangle(cornerRadius: 8))
                .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
                .onTapGesture {
                    onDismiss()
                }
        }
        .background(
            Color.clear
                .contentShape(Rectangle())
                .onTapGesture {
                    onDismiss()
                }
        )
    }
}
