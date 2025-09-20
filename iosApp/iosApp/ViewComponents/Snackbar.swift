//
//  Snackbar.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct Snackbar: View {
    let message: String
    @Binding var isShowing: Bool
    
    var body: some View {
        VStack {
            Spacer()
            
            if isShowing {
                HStack {
                    Text(message)
                        .foregroundColor(.white)
                        .font(.system(size: 14, weight: .medium))
                        .multilineTextAlignment(.leading)
                    
                    Spacer()
                    
                    Button(action: {
                        withAnimation(.easeInOut(duration: 0.3)) {
                            isShowing = false
                        }
                    }) {
                        Image(systemName: "xmark")
                            .foregroundColor(.white)
                            .font(.system(size: 12, weight: .bold))
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 12)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.black.opacity(0.8))
                )
                .padding(.horizontal, 20)
                .padding(.bottom, 50)
                .transition(.asymmetric(
                    insertion: .move(edge: .bottom).combined(with: .opacity),
                    removal: .move(edge: .bottom).combined(with: .opacity)
                ))
            }
        }
        .animation(.easeInOut(duration: 0.3), value: isShowing)
    }
}
