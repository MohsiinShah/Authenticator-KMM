//
//  RootContainer.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUICore
import SwiftUI

struct RootContainer: View {
    @State private var navPath = NavigationPath()
    
    @State var presentSideMenu = false
    
    @State private var currentDestination: Destination? = .SplashScreen
    
    var body: some View {
        NavigationStack(path: $navPath) {
            SplashScreen(navPath: $navPath)
                .navigationDestination(for: Destination.self) { destination in
                    switch destination {
                    case .SplashScreen:
                        SplashScreen(navPath: $navPath)
                            .onAppear { currentDestination = .SplashScreen }

                    case .DashboardScreen:
                        DashboardScreen(navPath: $navPath)
                            .onAppear { currentDestination = .DashboardScreen }
                            .navigationBarBackButtonHidden()
                    }
                }
        }
    }
}
