//
//  LoginViewModel.swift
//  VIBRA
//
//  Created by mac book pro on 11/7/25.
//
import Foundation
import Combine

@MainActor
final class LoginViewModel: ObservableObject {
    
    @Published var email = ""
    @Published var password = ""
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var isLoggedIn = false
    
    func login() async {
        isLoading = true
        errorMessage = nil

        do {
            let response = try await AuthService.shared.login(email: email, password: password)
            print("✅ Token reçu : \(response.access_token)")
            
            // Sauvegarder le token
            UserDefaults.standard.set(response.access_token, forKey: "jwt")
            
            // Marquer l'utilisateur comme connecté
            isLoggedIn = true
        } catch {
            errorMessage = "Email ou mot de passe incorrect."
            print("❌ Login error: \(error)")
        }

        isLoading = false
    }

}
