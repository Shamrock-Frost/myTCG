package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen
import java.net.InetAddress
import java.util.*

//Constants
internal val BOUNDS = Screen.getPrimary().visualBounds
internal val HEIGHT = BOUNDS.height
internal val WIDTH = BOUNDS.width

//Extension functions
internal operator fun Parent.plus(node: Node) = Group(this.childrenUnmodifiable + node)
internal fun Array<out Card?>.firstOpen() = this.indexOfFirst { it == null }
internal fun Array<CardWrapper?>.firstOpen() = this.indexOfFirst { it == null }
internal operator fun Parent.get(i: Int) = childrenUnmodifiable[i]
internal fun <E> stackOf(vararg es: E): Stack<E> {
    val result = Stack<E>()
    result.addAll(es)
    return result
}

const val PORT = 30725

//Global state is bad and I'm a bad programmer
//Nullable global state makes me feel dead inside
var isServer: Boolean? = null

fun DoNothing() = Unit
fun DoNothing(a1: Any) = Unit
fun DoNothing(a1: Any, a2: Any) = Unit

//TODO: Make these decks
internal val ServerDeck: Stack<CardWrapper> = stackOf()
internal val ClientDeck: Stack<CardWrapper> = stackOf()