package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.GUI.Styles.Companion.SCREEN_HEIGHT
import com.rhs.murphyTCG.GUI.Styles.Companion.wrapper
import com.rhs.murphyTCG.GUI.Styles.Companion.row
import javafx.scene.layout.VBox
import tornadofx.*

class BattleScreen : View() {
    override val root = VBox()

    init {
        title = "Pokemon: the Gatheringioh, Heroes of Vanguard"

        with(root) {
            addClass(wrapper)
            children.addClass(row)

            hbox {
                //layoutY = 0.0
                label("Username").layoutY = 200.0
                textfield()
            }

            hbox {
                layoutY = SCREEN_HEIGHT
                label("Password")
                textfield()
            }

            hbox {
                button("Login")
            }
        }
    }
}