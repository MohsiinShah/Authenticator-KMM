//
//  AddAccountManually.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AddAccountManuallyScreen: View {
    @Binding var navPath: NavigationPath
    let showSnackbar: (String) -> Void
    let accountCreated: () -> Void
    
    @StateObject private var addViewModel: AddViewModel = get()
    @FocusState private var isKeyboardFocused: Bool
    
    @State private var accountName: String = ""
    @State private var secretKey: String = ""
    @State private var checkTOTP: Bool = true
    @State private var checkHOTP: Bool = false
    @State private var additionalOptions: Bool = false
    @State private var intervalTOTP: String = "30"
    @State private var counterHOTP: String = "0"
    
    // Info popup states
    @State private var showPopupAccountName: Bool = false
    @State private var showPopupSecretKey: Bool = false
    @State private var showPopupAlgo: Bool = false
    @State private var showPopupTOTP: Bool = false
    @State private var showPopupHOTP: Bool = false
    
    var body: some View {
        ZStack {
            Color.white.ignoresSafeArea()
            
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    
                    // Account Name Section
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(alignment: .center) {
                            TextContent(
                                value: NSLocalizedString("account_name", comment: ""),
                                fontName: "ReadexPro-Regular",
                                fontSize: 14,
                                textColor: .black,
                                textAlign: .leading
                            )
                            
                            ZStack {
                                Button(action: {
                                    dismissAllPopups()
                                    showPopupAccountName = true
                                }) {
                                    Image(systemName: "info.circle")
                                        .foregroundColor(.gray)
                                        .frame(width: 24, height: 24)
                                }
                                
                                if showPopupAccountName {
                                    InfoBubble(
                                        text: NSLocalizedString("account_info", comment: ""),
                                        onDismiss: { showPopupAccountName = false }
                                    )
                                    .offset(x: -90, y: 35)
                                }
                            }
                            
                            Spacer()
                        }
                        
                        RoundedBorderedTextField(
                            value: $accountName,
                            hint: "Account name"
                        )
                        .focused($isKeyboardFocused)
                    }
                    
                    // Secret Key Section
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(alignment: .center) {
                            TextContent(
                                value: NSLocalizedString("secret_key", comment: ""),
                                fontName: "ReadexPro-Regular",
                                fontSize: 14,
                                textColor: .black,
                                textAlign: .leading
                            )
                            
                            ZStack {
                                Button(action: {
                                    dismissAllPopups()
                                    showPopupSecretKey = true
                                }) {
                                    Image(systemName: "info.circle")
                                        .foregroundColor(.gray)
                                        .frame(width: 24, height: 24)
                                }
                                
                                if showPopupSecretKey {
                                    InfoBubble(
                                        text: NSLocalizedString("secret_info", comment: ""),
                                        onDismiss: { showPopupSecretKey = false }
                                    )
                                    .offset(x: -90, y: 35)
                                }
                            }
                            
                            Spacer()
                        }
                        
                        RoundedBorderedTextField(
                            value: $secretKey,
                            hint: "Secret key",
                            isPassword: true
                        )
                        .focused($isKeyboardFocused)
                    }
                    
                    // Algorithm Section
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(alignment: .center) {
                            TextContent(
                                value: NSLocalizedString("algorithm", comment: ""),
                                fontName: "ReadexPro-Regular",
                                fontSize: 14,
                                textColor: .black,
                                textAlign: .leading
                            )
                            
                            ZStack {
                                Button(action: {
                                    dismissAllPopups()
                                    showPopupAlgo = true
                                }) {
                                    Image(systemName: "info.circle")
                                        .foregroundColor(.gray)
                                        .frame(width: 24, height: 24)
                                }
                                
                                if showPopupAlgo {
                                    InfoBubble(
                                        text: NSLocalizedString("algo_info", comment: "") +
                                        "HOTP (Counter-based One-Time Password)",
                                        onDismiss: { showPopupAlgo = false }
                                    )
                                    .offset(x: -90, y: 35)
                                }
                            }
                            
                            Spacer()
                        }
                        
                        VStack(spacing: 8) {
                            CustomCheckbox(
                                isChecked: Binding(
                                    get: { checkTOTP },
                                    set: { newValue in
                                        checkTOTP = newValue
                                        if newValue {
                                            checkHOTP = false
                                        }
                                    }
                                ),
                                text: NSLocalizedString("by_time_totp", comment: "")
                            )
                            
                            CustomCheckbox(
                                isChecked: Binding(
                                    get: { checkHOTP },
                                    set: { newValue in
                                        checkHOTP = newValue
                                        if newValue {
                                            checkTOTP = false
                                        }
                                    }
                                ),
                                text: NSLocalizedString("by_counter_hotp", comment: "")
                            )
                        }
                    }
                    
                    // Additional Options Section
                    if additionalOptions {
                        VStack(alignment: .leading, spacing: 10) {
                            if checkTOTP {
                                HStack(alignment: .center) {
                                    TextContent(
                                        value: "Refresh Interval",
                                        fontName: "ReadexPro-Light",
                                        fontSize: 12,
                                        textColor: .black,
                                        textAlign: .leading
                                    )
                                    
                                    Button(action: {
                                        dismissAllPopups()
                                        showPopupTOTP = true
                                    }) {
                                        Image(systemName: "info.circle")
                                            .foregroundColor(.gray)
                                            .frame(width: 24, height: 24)
                                    }
                                    
                                    Spacer()
                                }
                                
                                RoundedBorderedTextField(
                                    value: $intervalTOTP,
                                    hint: "30"
                                )
                                .frame(maxWidth: .infinity * 0.4)
                                .focused($isKeyboardFocused)
                                
                                if showPopupTOTP {
                                    InfoBubble(
                                        text: NSLocalizedString("totp_info", comment: ""),
                                        onDismiss: { showPopupTOTP = false }
                                    )
                                    .offset(x: 20)
                                }
                            } else {
                                HStack(alignment: .center) {
                                    TextContent(
                                        value: "Initial Counter",
                                        fontName: "ReadexPro-Light",
                                        fontSize: 12,
                                        textColor: .black,
                                        textAlign: .leading
                                    )
                                    
                                    Button(action: {
                                        dismissAllPopups()
                                        showPopupHOTP = true
                                    }) {
                                        Image(systemName: "info.circle")
                                            .foregroundColor(.gray)
                                            .frame(width: 24, height: 24)
                                    }
                                    
                                    Spacer()
                                }
                                
                                RoundedBorderedTextField(
                                    value: $counterHOTP,
                                    hint: "0"
                                )
                                .frame(maxWidth: .infinity * 0.4)
                                .focused($isKeyboardFocused)
                                
                                if showPopupHOTP {
                                    InfoBubble(
                                        text: NSLocalizedString("hotp_info", comment: ""),
                                        onDismiss: { showPopupHOTP = false }
                                    )
                                    .offset(x: 20)
                                }
                            }
                        }
                    }
                    
                    Spacer(minLength: 40)
                    
                    // Create Account Button
                    HStack {
                        Spacer()
                        ActionButton(
                            text: "Create Account",
                            width: 200,
                            action: {
                                if let validationError = secretKey.validateSecretKey() {
                                    showSnackbar(validationError)
                                } else {
                                    if checkTOTP {
                                        addViewModel.createTotp(
                                            name: accountName,
                                            secret: secretKey,
                                            interval: Int64(intervalTOTP) ?? 30
                                        )
                                    } else {
                                        addViewModel.createHotp(
                                            name: accountName,
                                            secret: secretKey,
                                            counter: Int64(counterHOTP) ?? 0
                                        )
                                    }
                                }
                            }
                        )
                        Spacer()
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 30)
                .padding(.bottom, 40)
            }
            .onTapGesture {
                dismissKeyboardAndPopups()
            }
        }
        .onAppear {
            Task {
                for await account in addViewModel.success {
                    await MainActor.run {
                        accountCreated()
                    }
                }
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    withAnimation {
                        navPath.removeLast()
                    }
                }) {
                    Image("ic_back")
                }
            }
            
            ToolbarItem(placement: .topBarLeading) {
                TextContent(
                    value: NSLocalizedString("add_account", comment: "Toolbar title"),
                    fontName: "ReadexPro-Medium",
                    fontSize: 20,
                    textColor: .white,
                    textAlign: .center
                )
            }
        }
        .toolbarBackground(Color("PrimaryBlue"), for: .navigationBar)
        .toolbarBackground(.visible, for: .navigationBar)
        .toolbarColorScheme(.light, for: .navigationBar)
    }
    
    private func dismissAllPopups() {
        showPopupAccountName = false
        showPopupSecretKey = false
        showPopupAlgo = false
        showPopupTOTP = false
        showPopupHOTP = false
    }
    
    private func dismissKeyboardAndPopups() {
        isKeyboardFocused = false
        dismissAllPopups()
    }
}
