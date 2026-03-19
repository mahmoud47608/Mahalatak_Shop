import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        KoinInitHelper.shared.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
