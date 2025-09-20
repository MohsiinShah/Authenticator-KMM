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
    private var accountsTask: Task<Void, Never>?

    @Published var accounts: [Account] = []

    init(viewModel: AccountsViewModel) {
        self.viewModel = viewModel
        observeAccounts()
    }
    
    private func observeAccounts() {
      accountsTask = Task { @MainActor in
            for await accounts in viewModel.accounts {
                self.accounts = accounts
            }
        }
        
    }
    
    deinit {
        accountsTask?.cancel()
    }
}
