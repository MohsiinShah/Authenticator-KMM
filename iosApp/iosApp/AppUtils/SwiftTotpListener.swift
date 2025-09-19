//
//  SwiftTotpListener.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import shared
import SwiftUI

// MARK: - TotpListener Implementation
class SwiftTotpListener: TotpListener {
    private let tickHandler: () -> Void
    
    init(onTick: @escaping () -> Void) {
        self.tickHandler = onTick
    }
    
    func onTick() {
        DispatchQueue.main.async {
            self.tickHandler()
        }
    }
}
