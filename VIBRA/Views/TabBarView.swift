//
//  TabBarView.swift
//  VIBRA
//
//  Created by mac book pro on 11/7/25.
//
// TabBarView.swift
import SwiftUI

struct TabBarView: View {
    var body: some View {
        TabView {
            HomeView()
                .tabItem {
                    Image(systemName: "house")
                    Text("Home")
                }
            
            Text("Map View")
                .tabItem {
                    Image(systemName: "map")
                    Text("Map")
                }
            
            Text("Community View")
                .tabItem {
                    Image(systemName: "person.2")
                    Text("Community")
                }
            
            Text("Add View")
                .tabItem {
                    Image(systemName: "plus")
                    Text("+")
                }
        }
        .accentColor(.green) // Couleur des icônes actives
        .background(Color.black) // Fond sombre
    }
}

#Preview {
    TabBarView()
}
