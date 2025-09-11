import SwiftUI
import VisionKit
import shared

struct ContentView: View {
    @StateObject private var viewModel: AccountsViewModel = get()
    @StateObject private var addAccountViewModel: AddViewModel = get()
    @State var isShowingScanner = true
    @State private var scannedText = ""
    @State private var account: Account? = nil
    @State private var accounts: [Account] = []


    var body: some View {
        if DataScannerViewController.isSupported && DataScannerViewController.isAvailable {
    
            ZStack(alignment: .bottom) {
                DataScannerRepresentable(
                    shouldStartScanning: $isShowingScanner,
                    scannedText: $scannedText,
                    dataToScanFor: [.barcode(symbologies: [.qr])]
                )

                Text(scannedText)
                    .padding()
                    .background(Color.white)
                    .foregroundColor(.black)
            }
            .onChange(of: scannedText, { oldvalue, newValue in
                addAccountViewModel.createByUri(uri: newValue)
            }
            
            )
            .onAppear{
                
                Task {
                    await observeError()
                }
            }
            .task {
                       for await list in viewModel.accounts {
                           self.accounts = list
                           list.forEach { account in
                               print(account)
                           }
                       }
                   }
        } else if !DataScannerViewController.isSupported {
            Text("It looks like this device doesn't support the DataScannerViewController")
        } else {
            Text("It appears your camera may not be available")
        }
    }
    
    private func observeError() async {
        Task {
            for await paginationState in addAccountViewModel.error {
                await MainActor.run {
                    print(paginationState.name)
                }
            }
        }
    }
}
