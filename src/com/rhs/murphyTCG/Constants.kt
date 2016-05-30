package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.Card
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen

internal val BOUNDS = Screen.getPrimary().visualBounds
internal val HEIGHT = BOUNDS.height
internal val WIDTH = BOUNDS.width

internal fun Parent.plus(node: Node) = Group(this.childrenUnmodifiable + node)
internal fun Array<out Card?>.firstOpen() = this.indexOfFirst { it == null }
internal operator fun Parent.get(i: Int) = childrenUnmodifiable[i]