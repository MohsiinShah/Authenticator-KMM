import SwiftUI
import shared

@main
struct iOSApp: App {
    @StateObject private var viewModel: AccountsViewModel = get()

    init() {

        let dependenciesHelper = DependenciesProviderHelper()
        dependenciesHelper.doInitKoinIos()
    }


    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
