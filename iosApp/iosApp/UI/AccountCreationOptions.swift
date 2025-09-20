//
//  AccountCreationOptions.swift
//  iosApp
//
//  Created by Mohsin on 19/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import SwiftUICore
import shared

struct AccountCreationOptions: View{
    
    @Binding var navPath: NavigationPath
    @State private var scannedCode: String = ""
    @State private var showScanner = false
    
    @StateObject private var viewModel: AccountsViewModel = get()
    @StateObject private var addAccountViewModel: AddViewModel = get()
    
    @Environment(\.dismiss) private var dismiss

    var body : some View{
        ZStack{
            AddAccountOptionsView{
                showScanner = true
            } onManualClicked: {
                navPath.append(Destination.AddAccountManuallyScreen)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
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
                                
                                TextContent(value: NSLocalizedString("qr_scan_title", comment: ""),
                                            fontName: "ReadexPro-Medium",
                                            fontSize: 20,
                                            textColor: .white,
                                            textAlign: .center)
                                .padding(.horizontal, 30)
                                
                                Spacer()
                            }
                            .padding(.top, 130)
                            .padding(.horizontal, 24)

                            Spacer()

                            HStack {
                                Spacer()
                                Button(action: {
                                    withAnimation{
                                        showScanner = false
                                    }
                                }) {
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
                        navPath.removeLast()
                    }
                }) {
                    Image("ic_back")
                }
            }
            
            ToolbarItem(placement: .topBarLeading) {
                TextContent(
                    value:  NSLocalizedString("add_account", comment: "Toolbar title"),
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
    }
}
