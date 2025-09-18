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

