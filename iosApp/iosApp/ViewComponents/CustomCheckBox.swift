//
//  CustomCheckBox.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUICore
import SwiftUI

// MARK: - Custom Checkbox
struct CustomCheckbox: View {
    @Binding var isChecked: Bool
    let text: String
    
    var body: some View {
        HStack(alignment: .center) {
            Button(action: {
                isChecked.toggle()
            }) {
                ZStack {
                    RoundedRectangle(cornerRadius: 3)
                        .stroke(isChecked ? Color("PrimaryBlue") : Color.gray.opacity(0.5), lineWidth: 2)
                        .background(
                            RoundedRectangle(cornerRadius: 3)
                                .fill(isChecked ? Color("PrimaryBlue") : Color.clear)
                        )
                        .frame(width: 20, height: 20)
                    
                    if isChecked {
                        Image(systemName: "checkmark")
                            .foregroundColor(.white)
                            .font(.system(size: 12, weight: .bold))
                    }
                }
            }
            .buttonStyle(PlainButtonStyle())
            
            TextContent(
                value: text,
                fontName: "ReadexPro-Light",
                fontSize: 12,
                textColor: .black,
                textAlign: .leading
            )
            
            Spacer()
        }
    }
}
