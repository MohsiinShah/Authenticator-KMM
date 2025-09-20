//
//  TotpTimerManager.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

// MARK: - TotpTimer Manager for SwiftUI
class TotpTimerManager: ObservableObject {
    static let shared = TotpTimerManager()
    private var listeners: [String: SwiftTotpListener] = [:]
    
    private init() {
        // Start the timer when the manager is created
        TotpTimer.shared.start()
    }
    
    func subscribe(id: String, onTick: @escaping () -> Void) {
        let listener = SwiftTotpListener(onTick: onTick)
        listeners[id] = listener
        TotpTimer.shared.subscribe(listener: listener)
    }
    
    func unsubscribe(id: String) {
        if let listener = listeners[id] {
            TotpTimer.shared.unsubscribe(listener: listener)
            listeners.removeValue(forKey: id)
        }
    }
    
    deinit {
        TotpTimer.shared.unsubscribeAll()
    }
}
