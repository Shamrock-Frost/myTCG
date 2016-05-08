package com.rhs.murphyTCG

import com.rhs.murphyTCG.Styles.Companion.wrapper
import com.rhs.murphyTCG.Styles.Companion.password
import com.rhs.murphyTCG.Styles.Companion.username
import com.rhs.murphyTCG.Styles.Companion.row
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
                addClass(username)
                label("Username")
                textfield()
            }

            hbox {
                addClass(password)
                label("Password")
                textfield()
            }

            hbox {
                button("Login")
            }
        }
    }
}