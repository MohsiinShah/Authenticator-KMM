//
//  DashboardScreen.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUICore
import SwiftUI
import shared

struct DashboardScreen: View{
    @Binding var navPath: NavigationPath
    @State private var scannedCode: String = ""
    @State private var showScanner = false
    
    @StateObject private var viewModel: AccountsViewModel = get()
    @StateObject private var addAccountViewModel: AddViewModel = get()
    
    @StateObject var accountsObserver = AccountsObserver(viewModel: get())
    @StateObject var accountCreationObserver = AccountCreationObserver(viewModel: get())
    
    
    var body : some View{
        
        
        ZStack{
            if(accountsObserver.accounts.isEmpty){
                VStack(alignment: .center, spacing: 20) {
                    AddAccountOptionsView {
                        showScanner = true
                    } onManualClicked: {
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .ignoresSafeArea(.all, edges: .top)
            }else{
                ScrollView {
                    LazyVStack(spacing: 8) {
                        ForEach(accountsObserver.accounts.indices, id: \.self) { i in
                            AccountsView(account: accountsObserver.accounts[i])
                        }
                    }
                    .padding(.top, 16)
                }
                
                
                VStack {
                    Spacer()
                    HStack {
                        Spacer()
                        FloatingActionButton(icon: "ic_plus") {
                            withAnimation{
                                navPath.append(Destination.AccountOptionsScreen)
                            }
                        }
                        .padding(.trailing, 20)
                        .padding(.bottom, 40)
                    }
                }
            }
            
            Snackbar(
                message: accountCreationObserver.message,
                isShowing: $accountCreationObserver.showSnackbar
            )
            .zIndex(1)
        }
        .fullScreenCover(isPresented: $showScanner, content: {
            ZStack {
                QRScannerView { code in
                    scannedCode = code
                    showScanner = false
                    addAccountViewModel.createByUri(uri: code)
                }

                // Dim overlay with cutout
                GeometryReader { proxy in
                    let screen = proxy.size
                    let side = min(screen.width, screen.height) * 0.6
                    let rect = CGRect(x: (screen.width - side) / 2,
                                      y: (screen.height - side) / 2,
                                      width: side,
                                      height: side)

                    Color.black.opacity(0.55)
                        .mask(
                            Rectangle()
                                .overlay(
                                    Rectangle()
                                        .blendMode(.destinationOut)
                                        .frame(width: rect.width, height: rect.height)
                                        .position(x: rect.midX, y: rect.midY)
                                )
                                .compositingGroup()
                        )
                        .ignoresSafeArea()
                }

                // Top tagline and Cancel
                VStack(spacing: 0) {
                    HStack {
                        Spacer()
                        TextContent(value: "Align the QR within the box to scan",
                                    fontName: "ReadexPro-Medium",
                                    fontSize: 16,
                                    textColor: .white,
                                    textAlign: .center)
                        Spacer()
                    }
                    .padding(.top, 50)
                    .padding(.horizontal, 24)

                    Spacer()

                    HStack {
                        Button(action: { showScanner = false }) {
                            TextContent(value: "Cancel",
                                        fontName: "ReadexPro-Medium",
                                        fontSize: 16,
                                        textColor: .white,
                                        textAlign: .leading)
                                .padding(.horizontal, 16)
                                .padding(.vertical, 10)
                                .background(Color.black.opacity(0.5))
                                .clipShape(RoundedRectangle(cornerRadius: 10))
                        }
                        Spacer()
                    }
                    .padding(.horizontal, 16)
                    .padding(.bottom, 30)
                }
            }
            .ignoresSafeArea()
        })
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    withAnimation {
                    }
                }) {
                    Image("ic_hamburger")
                }
            }
            
            ToolbarItem(placement: .topBarLeading) {
                TextContent(
                    value: NSLocalizedString("app_name", comment: "Toolbar title"),
                    fontName: "ReadexPro-Medium",
                    fontSize: 20,
                    textColor: .white,
                    textAlign: .center
                )
            }
            
        }
        .toolbarBackground(Color("PrimaryBlue"),
                           for: .navigationBar)
        .toolbarBackground(.visible,
                           for: .navigationBar)
        .toolbarColorScheme(.light, for: .navigationBar)
        .frame(width: .infinity, height: .infinity)
        
    }
}


struct AddAccountOptionsView: View{
    var onScanClicked: () -> Void
    var onManualClicked: () -> Void
    
    var body: some View{
        
        VStack{
            CustomLottieAnimation(fileName: "dashboard_anim", width: 145, height: 130 )
            
            Image("first_2fa")
            
            TextContent(value: NSLocalizedString("dashboard_tagline", comment: "dashboard tag line"),
                        fontName: "ReadexPro-Light", fontSize: 14, textColor: Color("Black40"), textAlign: .center)
            .padding(.horizontal, 30)
            
            ActionButton(
                    text: NSLocalizedString("scan_qr", comment: "QR scan button"),
                    width: 200,
                    action: {
                        onScanClicked()
                    }
                )
            
            ActionButtonBordered(
                    text: NSLocalizedString("enter_manually", comment: "Manual account creation button"),
                    width: 200,
                    action: {
                        onManualClicked()
                    }
                )
        }
    }
}


struct FloatingActionButton: View {
    var icon: String
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Image(icon)
                .resizable()
                .scaledToFit()
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
                .padding(20)
                .background(Color("PrimaryBlue"))
                .clipShape(Circle())
                .shadow(radius: 5)
        }
    }
}


struct AccountsView: View {
    var account: Account
    @State private var password: String = ""
    @State private var secondsLeft: Int = 0
    @State private var progress: Double = 0.0
    @State private var intervalState: IntervalState = .high

    var body: some View {
        ZStack{
            
            VStack(spacing: 4){
                HStack{
                    ZStack{
                        CircularProgressBar(progress: progress, color: intervalState.color)
                        
                        ZStack{
                            
                            TextContent(value: String(secondsLeft),
                                        fontName: "ReadexPro-SemiBold", fontSize: 14,
                                        textColor: Color.white,
                                        textAlign: .center)
                            
                            
                        }
                        .frame(width: 50, height: 50)
                        .background(intervalState.color)
                        .clipShape(RoundedRectangle(cornerRadius: 50))
                    }
                    
                    
                    VStack(alignment: .leading, spacing: 6){
                        
                        TextContent(value: account.issuer ?? "Account",
                                    fontName: "ReadexPro-SemiBold", fontSize: 14,
                                    textColor: Color("PrimaryBlue"),
                                    textAlign: .leading)
                        
                        TextContent(value: account.name,
                                    fontName: "ReadexPro-Regular", fontSize: 12,
                                    textColor: Color.black,
                                    textAlign: .leading)
                        
                    }
                    
                    Spacer()
                    
                }
                .frame(width: .infinity, alignment: .leading)
                .padding(.leading, 16)
                .padding(.top, 12)
                
                PasswordRow(password: password)
            }
        }
        .frame(maxWidth: .infinity)
        .background(
        RoundedRectangle(cornerRadius: 14)
            .stroke(Color("BorderGrey"), lineWidth: 1)
                 )
        .padding(.horizontal, 20)
        .onAppear {
            updateAccountState() // Initial state update
            if let totp = account as? TotpAccount {
                TotpTimerManager.shared.subscribe(id: account.label) {
                    updateAccountState()
                }
            }
        }
        .onDisappear {
            TotpTimerManager.shared.unsubscribe(id: account.label)
        }
    }
    
    private func updateAccountState() {
        password = account.password.formatAsOtp()
        
        if let totp = account as? TotpAccount {
            totp.update()
            password = totp.password.formatAsOtp()
            secondsLeft = Int(totp.secondsRemain())
            progress = Double(totp.secondsRemain()) / Double(totp.interval) // For anticlockwise
            
            // Update interval state immediately without animation flicker
            let newIntervalState: IntervalState = if (secondsLeft > 20) {
                .high
            } else if (secondsLeft > 10) {
                .medium
            } else {
                .low
            }
            
            // Set state without animation to prevent flicker
            withAnimation(.none) {
                intervalState = newIntervalState
            }
        }
    }
}


enum IntervalState {
    case high, medium, low
    
    var color: Color {
        switch self {
        case .high: return .green
        case .medium: return .orange
        case .low: return .red
        }
    }
}

struct PasswordRow: View {
    var password: String
    
    var body: some View {
    
        HStack(alignment: .top, spacing: 2){
            ForEach(Array(password.enumerated()), id: \.offset) { idx, ch in
                AnimatedOtpDigit(index: idx,
                                 digit: ch,
                                 fontName: "ReadexPro-Regular",
                                 fontSize: 35,
                                 textColor: Color("PrimaryBlue"))
            }
            Spacer()
        }
        .padding(.leading, 75)
        .padding(.bottom, 12)
        
    }
}


struct CircularProgressBar: View {
    var progress: Double // 0.0 to 1.0
    var lineWidth: CGFloat = 4
    var color: Color = Color("PrimaryBlue")
    
    @State private var animatedProgress: Double = 0.0
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(Color.gray.opacity(0.3), lineWidth: lineWidth)
            
            Circle()
                .trim(from: 1.0 - animatedProgress, to: 1.0)
                .stroke(color, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
                .rotationEffect(.degrees(-90))
        }
        .frame(width: 60, height: 60)
        .onAppear {
            animatedProgress = progress
        }
        .onChange(of: progress) { newProgress in
            withAnimation(.easeInOut(duration: 1.0)) {
                animatedProgress = newProgress
            }
        }
    }
}


struct AnimatedOtpDigit: View {
    let index: Int
    let digit: Character
    var fontName: String
    var fontSize: CGFloat
    var textColor: Color

    @State private var shownDigit: Character = "0"
    @State private var opacity: Double = 0.0
    @State private var offsetY: CGFloat = 8
    @State private var scale: CGFloat = 0.92

    var body: some View {
        TextContent(value: String(shownDigit),
                    fontName: fontName,
                    fontSize: fontSize,
                    textColor: textColor,
                    textAlign: .center)
            .opacity(opacity)
            .offset(y: offsetY)
            .scaleEffect(scale)
            .onAppear {
                shownDigit = digit
                animateIn(stagger: 0.02 * Double(index))
            }
            .onChange(of: digit) { newVal in
                shownDigit = newVal
                // prominent yet nice: quick slide+fade+scale with slight stagger
                opacity = 0.0
                offsetY = 8
                scale = 0.92
                animateIn(stagger: 0.02 * Double(index))
            }
    }

    private func animateIn(stagger: Double) {
        DispatchQueue.main.asyncAfter(deadline: .now() + stagger) {
            withAnimation(.spring(response: 0.28, dampingFraction: 0.85)) {
                opacity = 1.0
                offsetY = 0
                scale = 1.0
            }
        }
    }
}

// (Reverted animated components)

