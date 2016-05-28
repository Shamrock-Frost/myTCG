package com.rhs.murphyTCG

import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen

val BOUNDS = Screen.getPrimary().visualBounds
val HEIGHT = BOUNDS.height
val WIDTH = BOUNDS.width

internal fun Parent.plus(node: Node) = Group(this.childrenUnmodifiable + node)