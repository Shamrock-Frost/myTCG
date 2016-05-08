package com.rhs.murphyTCG

import javafx.geometry.Rectangle2D
import javafx.scene.paint.Color
import javafx.stage.Screen
import tornadofx.*
import tornadofx.LinearDimension.Units

class Styles : Stylesheet() {
    val SCREEN_BOUNDS : Rectangle2D = Screen.getPrimary().visualBounds
    val SCREEN_HEIGHT : Double = SCREEN_BOUNDS.height
    val SCREEN_WIDTH  : Double = SCREEN_BOUNDS.width

    companion object {
        val wrapper  by cssclass()
        val row      by cssclass()
        val username by cssclass()
        val password by cssclass()
    }

    init {
        val flat = mixin {
            backgroundInsets = box(0.px)
            borderColor = box(Color.LIGHTGRAY)
        }

        s(wrapper) {
            s(label) {
                minWidth = 100.px
            }

            s(row) {
                padding = box(10.px)
            }

            s(username) {

            }

            s(password) {

            }

            padding = box(15.px)
            minWidth = LinearDimension(SCREEN_WIDTH * 0.9, Units.px)
            minHeight = LinearDimension(SCREEN_HEIGHT * 0.9, Units.px)
        }

        s(button, textInput) {
            +flat
        }
    }
}