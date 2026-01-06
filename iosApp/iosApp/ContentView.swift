import SwiftUI
import Shared

struct ContentView: View {
    @State private var showContent = false
    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()
            VStack {
                
                HeaderView()
                CharacterCardView()
                TabMenuView(selected: .constant(.equipment))
                EquipmentGridView()
                Spacer()
                BottomTabBarView()
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .padding()
        }
    }
}





struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
