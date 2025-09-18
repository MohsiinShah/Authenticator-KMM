//
//  AccountsObserver.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared
import Combine

class AccountsObserver: ObservableObject {
    private let viewModel: AccountsViewModel
    private var cancellables = Set<AnyCancellable>()

    @Published var accounts: [Account] = []

    init(viewModel: AccountsViewModel) {
        self.viewModel = viewModel
        observeAccounts()
    }
    
    private func observeAccounts() {
        // Option 1: Using Skie's asyncSequence (recommended)
        Task { @MainActor in
            for await accounts in viewModel.accounts {
                self.accounts = accounts
            }
        }
    }
    
    deinit {
        cancellables.removeAll()
    }
}
