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
    
    @StateObject var accountsObserver = AccountsObserver(viewModel: get()) // inject KMM VM
    
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
                            print("FAB tapped")
                        }
                        .padding(.trailing, 20)
                        .padding(.bottom, 40)
                    }
                }
            }
        }
        .fullScreenCover(isPresented: $showScanner, content: {
            QRScannerView{ code in
                scannedCode = code
                showScanner = false
                addAccountViewModel.createByUri(uri: code)

            }
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
            password = account.password.formatAsOtp()
            if let totp = account as? TotpAccount {
                secondsLeft = Int(totp.secondsRemain())
                progress = 1.0 - (Double(totp.secondsRemain()) / Double(totp.interval))
                TotpTimerManager.shared.subscribe(id: account.label) {
                    totp.update()
                    password = totp.password.formatAsOtp()
                    secondsLeft = Int(totp.secondsRemain())
                    progress = 1.0 - (Double(totp.secondsRemain()) / Double(totp.interval))
                    
                    intervalState = if (secondsLeft > 20) {
                        IntervalState.high
                    }
                    else if (secondsLeft > 10){
                        IntervalState.medium
                    }
                    else{
                        IntervalState.low
                    }
                }
            } else {
                // HOTP or other types; keep static password for now
                password = account.password.formatAsOtp()
            }
        }
        .onDisappear {
            TotpTimerManager.shared.unsubscribe(id: account.label)
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
    
        HStack(alignment: .top){
            TextContent(value: password,
                        fontName: "ReadexPro-Regular", fontSize: 35,
                        textColor: Color("PrimaryBlue"),
                        textAlign: .leading)
            
            Spacer()
        }
        .padding(.leading, 75)
        .padding(.bottom, 12)
        
    }
}

#Preview{
  //  AccountsView()
}

#Preview{
 //   PasswordRow()
}

struct CircularProgressBar: View {
    var progress: Double // 0.0 to 1.0
    var lineWidth: CGFloat = 4
    var color: Color = Color("PrimaryBlue")
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(Color.gray.opacity(0.3), lineWidth: lineWidth) // background circle
            
            Circle()
                .trim(from: 0, to: progress) // foreground progress
                .stroke(color, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
                .rotationEffect(.degrees(-90)) // start from top
                .animation(.easeInOut(duration: 0.5), value: progress)
        }
        .frame(width: 60, height: 60)
    }
}


