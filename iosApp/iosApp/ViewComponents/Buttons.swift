
//
//  Buttons.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct ActionButton: View {
    var text: String
    var width: CGFloat = .infinity
    var height: CGFloat = 47
    var textSize: CGFloat = 17
    var fontName: String = "ReadexPro-Medium"
    var backgroundColor: Color = Color("PrimaryBlue")  // from Assets
    var textColor: Color = .white
    var cornerRadius: CGFloat = 10
    var action: () -> Void = {}
    
    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.custom(fontName, size: textSize))
                .foregroundColor(textColor)
                .frame(maxWidth: width, minHeight: height)
                .background(backgroundColor)
                .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
        }
    }
}


struct ActionButtonBordered: View {
    var text: String
    var width: CGFloat = .infinity
    var height: CGFloat = 47
    var textSize: CGFloat = 17
    var fontName: String = "ReadexPro-Medium"
    var borderColor: Color = Color("PrimaryBlue")
    var backgroundColor: Color = .white
    var textColor: Color = Color("PrimaryBlue")
    var cornerRadius: CGFloat = 10
    var action: () -> Void = {}
    
    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.custom(fontName, size: textSize))
                .foregroundColor(textColor)
                .frame(maxWidth: width, minHeight: height)
                .background(backgroundColor)
                .overlay(
                    RoundedRectangle(cornerRadius: cornerRadius)
                        .stroke(borderColor, lineWidth: 1)
                )
                .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
        }
    }
}
