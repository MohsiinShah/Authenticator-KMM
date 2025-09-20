//
//  AccountCreationObserver.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import shared
import SwiftUI

@MainActor
final class AccountCreationObserver: ObservableObject {
    private let viewModel: AddViewModel
    private var accountCreationTask: Task<Void, Never>?
    private var accountErrorTask: Task<Void, Never>?

    @Published var message: String = ""
    @Published var showSnackbar: Bool = false
    
    private var hideTimer: Timer?

    init(viewModel: AddViewModel) {
        self.viewModel = viewModel
        observeAccountCreation()
        observeAccountErrors()
    }

    private func presentSnackbar(_ text: String) {
        hideTimer?.invalidate()
        message = text
        withAnimation(.easeInOut(duration: 0.3)) {
            showSnackbar = true
        }
        hideTimer = Timer.scheduledTimer(withTimeInterval: 3.0, repeats: false) { [weak self] _ in
            Task { @MainActor in
                withAnimation(.easeInOut(duration: 0.3)) {
                    self?.showSnackbar = false
                }
            }
        }
    }

    private func observeAccountCreation() {
        accountCreationTask = Task { [weak self] in
            do {
                for await name in viewModel.successMessage {
                    guard let self = self else { return }
                    let prefix = NSLocalizedString("authorization_enabled", comment: "")
                    let text = "\(prefix) \(name)"
                    await MainActor.run {
                        self.presentSnackbar(text)
                    }
                }
            } catch {
                print("Error observing account creation: \(error)")
            }
        }
    }

    private func observeAccountErrors() {
        accountErrorTask = Task { [weak self] in
            do {
                for await err in viewModel.error {
                    guard let self = self else { return }
                    let errorMessage = self.message(for: err)
                    await MainActor.run {
                        self.presentSnackbar(errorMessage)
                    }
                }
            } catch {
                print("Error observing account errors: \(error)")
            }
        }
    }

    private func message(for error: CreationError) -> String {
        switch error {
        case .alreadyExists:
            return NSLocalizedString("error_account_exists", comment: "Account already exists")
        case .shortLabel:
            return NSLocalizedString("error_short_label", comment: "Label is too short")
        case .shortSecret:
            return NSLocalizedString("error_short_secret", comment: "Secret is too short")
        case .invalidUri:
            return NSLocalizedString("error_invalid_uri", comment: "Invalid QR/URI")
        case .undefinedType:
            return NSLocalizedString("error_undefined_type", comment: "Unsupported account type")
        case .unsupportedAlgorithm:
            return NSLocalizedString("error_unsupported_algorithm", comment: "Unsupported algorithm")
        case .invalidInterval:
            return NSLocalizedString("error_invalid_interval", comment: "Invalid TOTP interval")
        case .intervalMaxLimit:
            return NSLocalizedString("error_interval_limit", comment: "Interval can't exceed 60 seconds")
        case .invalidCounter:
            return NSLocalizedString("error_invalid_counter", comment: "Invalid HOTP counter")
        case .undefined:
            fallthrough
        @unknown default:
            return NSLocalizedString("error_generic", comment: "Something went wrong")
        }
    }
    
    func hideSnackbar() {
        hideTimer?.invalidate()
        withAnimation(.easeInOut(duration: 0.3)) {
            showSnackbar = false
        }
    }

    deinit {
        hideTimer?.invalidate()
        accountCreationTask?.cancel()
        accountErrorTask?.cancel()
    }
}
