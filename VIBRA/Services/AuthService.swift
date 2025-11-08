//
//  AuthService.swift
//  VIBRA
//
//  Created by mac book pro on 11/7/25.
//
import Foundation

// MARK: - AuthService
final class AuthService {
    
    static let shared = AuthService()
    private init() {}
    
    // MARK: - LOGIN
    func login(email: String, password: String) async throws -> AuthResponse {
        guard let url = URL(string: "\(Constants.baseURL)/auth/login") else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body = ["email": email, "password": password]
        request.httpBody = try JSONEncoder().encode(body)
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        guard (200...299).contains(httpResponse.statusCode) else {
            let serverMessage = String(data: data, encoding: .utf8) ?? "Unknown error"
            print("❌ Server error \(httpResponse.statusCode): \(serverMessage)")
            throw URLError(.badServerResponse)
        }
        
        return try JSONDecoder().decode(AuthResponse.self, from: data)
    }
    
    // MARK: - REGISTER
    func register(firstName: String,
                  lastName: String,
                  gender: String,
                  email: String,
                  password: String,
                  avatar: String? = nil,
                  role: String? = nil) async throws -> User {
        
        guard let url = URL(string: "\(Constants.baseURL)/user") else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Body JSON correspond exactement au DTO NestJS
        var body: [String: Any] = [
            "firstName": firstName,
            "lastName": lastName,
            "Gender": gender, // ✅ majuscule pour correspondre au backend
            "email": email,
            "password": password
        ]
        
        if let avatar = avatar { body["avatar"] = avatar }
        if let role = role { body["role"] = role }
        
        request.httpBody = try JSONSerialization.data(withJSONObject: body, options: [])
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        guard (200...299).contains(httpResponse.statusCode) else {
            let serverMessage = String(data: data, encoding: .utf8) ?? "Unknown error"
            print("❌ Server error \(httpResponse.statusCode): \(serverMessage)")
            throw URLError(.badServerResponse)
        }
        
        return try JSONDecoder().decode(User.self, from: data)
    }
}
