
//
//  LottieView.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Lottie


struct CustomLottieAnimation: View {
    let fileName: String
    let width: CGFloat
    let height: CGFloat
    var body: some View {
        LottieView(animation: .named(fileName))
            .configure { animationView in
                animationView.contentMode = .scaleAspectFill
                animationView.loopMode = .loop
                animationView.play()
            }
            .frame(maxWidth: width, maxHeight: height)
            .accessibilityLabel("Main Feature Animation")
        
    }
}

