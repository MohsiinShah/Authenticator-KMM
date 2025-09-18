//
//  SplashScreen.swift
//  iosApp
//
//  Created by Mohsin on 18/09/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUICore
import SwiftUI

struct SplashScreen: View{
    @Binding var navPath: NavigationPath

    var body: some View{
        
        ZStack{
            
            VStack(alignment: .center, spacing: 20) {
                
                Spacer()
                
                Image("ic_splash")
                
                TextContent(value: NSLocalizedString("app_name", comment: "Welcome text on splash"),
                            fontName: "ReadexPro-Regular", fontSize: 22, textAlign: .center)
                                
                
                TextContent(value: NSLocalizedString("splash_tagline", comment: "Splash tag line"),
                            fontName: "ReadexPro-Light", fontSize: 15, textColor: Color("Black40"), textAlign: .center)
                .padding(.horizontal, 30)
                
                
                Spacer()
                
                ActionButton(
                        text: NSLocalizedString("get_started", comment: "Get started button on splash"),
                        action: {
                            navPath.append(Destination.DashboardScreen)
                        }
                    )
                .frame(width: .infinity)
                .padding(.horizontal, 20)
                .padding(.bottom, 70)

            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

        }
        .frame(width: .infinity, height: .infinity)
        .background(Color.white)
        
    }

}
