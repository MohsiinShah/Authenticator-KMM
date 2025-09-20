//
//  TextContent.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct TextContent: View {
    var value: String
    var fontName: String = "ReadexPro_Regular"
    var fontSize: CGFloat = 14
    var textColor: Color = .black
    var textAlign: TextAlignment = .leading
    var horizontalPadding: CGFloat = 0
    var verticalPadding: CGFloat = 0
    
    var body: some View {
        Text(value)
            .font(fontName == "System"
                  ? .system(size: fontSize)
                  : .custom(fontName, size: fontSize))
            .foregroundColor(textColor)
            .multilineTextAlignment(textAlign)
            .padding(.horizontal, horizontalPadding)
            .padding(.vertical, verticalPadding)
        
    }
}

struct RoundedBorderedTextField: View {
    @Binding var value: String
    var hint: String = "Enter text"
    var isPassword: Bool = false
    
    @State private var isPasswordVisible: Bool = false
    
    var body: some View {
        HStack {
            if isPassword && !isPasswordVisible {
                SecureField(hint, text: $value)
                    .textFieldStyle()
            } else {
                TextField(hint, text: $value, axis: .vertical)
                    .lineLimit(1...4)
                    .textFieldStyle()
            }
            
            if isPassword {
                Button(action: {
                    isPasswordVisible.toggle()
                }) {
                    Image(systemName: isPasswordVisible ? "eye" : "eye.slash")
                        .foregroundColor(.gray)
                        .frame(width: 20, height: 20)
                }
                .padding(.trailing, 8)
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color.white)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color("BorderGrey"), lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}

private extension View {
    func textFieldStyle() -> some View {
        self
            .font(.custom("ReadexPro-Light", size: 16))
            .foregroundColor(Color.black)
            .accentColor(Color("PrimaryBlue"))
    }
}
