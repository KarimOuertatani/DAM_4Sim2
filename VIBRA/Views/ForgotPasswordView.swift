//
//  ForgotPasswordView.swift
//  VIBRA
//
//  Created by mac book pro on 11/6/25.
//

import SwiftUI

// MARK: - ForgotPasswordView
struct ForgotPasswordView: View {
    // MARK: - States
    @State private var email = ""
    @State private var isLinkSent = false

    // MARK: - Body
    var body: some View {
        ZStack {
            // MARK: Background
            Image("login_bg")
                .resizable()
                .scaledToFill()
                .ignoresSafeArea()

            Color.black.opacity(0.55)
                .ignoresSafeArea()

            // MARK: - Main Content
            ScrollView {
                VStack(spacing: 24) {
                    Spacer(minLength: 40)

                    // MARK: - Header (Logo + textes)
                    VStack(spacing: 10) {
                        Image("logo")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 60, height: 60)
                            .shadow(radius: 8)
                            .padding(.bottom, 4)

                        Text("Forgot your password?")
                            .foregroundColor(.white)
                            .font(.title.bold())

                        Text("Enter your email to reset it")
                            .foregroundColor(.green.opacity(0.8))
                            .font(.subheadline)
                    }
                    .multilineTextAlignment(.center)

                    // MARK: - Email Field
                    VStack(spacing: 16) {
                        HStack {
                            Image(systemName: "envelope")
                                .foregroundColor(.green)
                            ZStack(alignment: .leading) {
                                if email.isEmpty {
                                    Text("Enter your email")
                                        .foregroundColor(.green.opacity(0.7))
                                }
                                TextField("", text: $email)
                                    .textInputAutocapitalization(.never)
                                    .keyboardType(.emailAddress)
                                    .foregroundColor(.white)
                                    .accentColor(.green)
                            }
                        }
                        .padding()
                        .background(Color.black.opacity(0.4))
                        .cornerRadius(10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(Color.white.opacity(0.1))
                        )
                    }

                    // MARK: - Reset Button
                    Button(action: {
                        withAnimation {
                            isLinkSent = true
                        }
                    }) {
                        HStack {
                            Text("Send reset link")
                                .fontWeight(.semibold)
                            Image(systemName: "arrow.right")
                        }
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(email.isEmpty ? Color.gray : Color.green)
                        .cornerRadius(10)
                    }
                    .disabled(email.isEmpty)

                    // MARK: - Confirmation Message
                    if isLinkSent {
                        Text("✅ A reset link has been sent to your email.")
                            .foregroundColor(.green)
                            .font(.footnote)
                            .transition(.opacity)
                            .padding(.top, 8)
                    }

                    // MARK: - Back to Login
                    HStack(spacing: 4) {
                        Text("Remember your password?")
                            .foregroundColor(.white.opacity(0.8))
                        NavigationLink(destination: LoginView()) {
                            Text("Log in")
                                .foregroundColor(.green)
                        }
                    }
                    .font(.footnote)

                    Spacer(minLength: 40)
                }
                .padding(.horizontal, 30)
                .frame(maxWidth: 400)
                .frame(maxWidth: .infinity)
                .multilineTextAlignment(.center)
            }
        }
    }
}

// MARK: - Preview
#Preview {
    ForgotPasswordView()
}
