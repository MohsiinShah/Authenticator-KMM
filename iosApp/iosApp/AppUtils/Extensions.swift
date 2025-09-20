
import KMPObservableViewModelCore
import shared
import SwiftUICore

extension Kmp_observableviewmodel_coreViewModel: KMPObservableViewModelCore.ViewModel {}


// MARK: - Secret Key Validation
extension String {
    func validateSecretKey() -> String? {
        let secret = self.trimmingCharacters(in: .whitespaces)
            .replacingOccurrences(of: " ", with: "")
            .uppercased()
        
        if secret.isEmpty {
            return "Secret key cannot be empty"
        }
        
        let base32Regex = try! NSRegularExpression(pattern: "^[A-Z2-7]+=*$")
        let range = NSRange(location: 0, length: secret.utf16.count)
        
        if base32Regex.firstMatch(in: secret, options: [], range: range) == nil {
            return "Secret key must only contain A-Z, digits 2-7, and optional '=' padding"
        }
        
        do {
            try decodeBase32(secret)
            let secretWithoutPadding = secret.trimmingCharacters(in: CharacterSet(charactersIn: "="))
            
            if secretWithoutPadding.count < 16 {
                return "Secret key too short. Minimum 16 characters required"
            }
            
            return nil
        } catch {
            return "Invalid Base32 encoding: \(error.localizedDescription)"
        }
    }
    
    private func decodeBase32(_ input: String) throws -> Data {
        let alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        let cleanInput = input.trimmingCharacters(in: CharacterSet(charactersIn: "="))
        
        for char in cleanInput {
            if !alphabet.contains(char) {
                throw NSError(domain: "Base32Error", code: 1, userInfo: [NSLocalizedDescriptionKey: "Invalid Base32 character"])
            }
        }
        
        let bits = cleanInput.compactMap { char -> String? in
            guard let index = alphabet.firstIndex(of: char) else { return nil }
            let value = alphabet.distance(from: alphabet.startIndex, to: index)
            return String(value, radix: 2).leftPadding(toLength: 5, withPad: "0")
        }.joined()
        
        var bytes = [UInt8]()
        for i in stride(from: 0, to: bits.count, by: 8) {
            let endIndex = min(i + 8, bits.count)
            if endIndex - i == 8 {
                let byteString = String(bits[bits.index(bits.startIndex, offsetBy: i)..<bits.index(bits.startIndex, offsetBy: endIndex)])
                if let byte = UInt8(byteString, radix: 2) {
                    bytes.append(byte)
                }
            }
        }
        
        return Data(bytes)
    }
}

private extension String {
    func leftPadding(toLength: Int, withPad character: Character) -> String {
        let stringLength = self.count
        if stringLength < toLength {
            return String(repeatElement(character, count: toLength - stringLength)) + self
        } else {
            return String(self.suffix(toLength))
        }
    }
}
