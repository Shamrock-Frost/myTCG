package com.rhs.murphyTCG

import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.hbox
import tornadofx.label

class BattleScreen : View() {
    override val root = VBox()

    init {
        with(root) {
            hbox {
                label("Hello World!")
            }
        }
    }
}