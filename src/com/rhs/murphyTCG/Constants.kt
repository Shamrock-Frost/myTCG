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
internal val BOUNDS = javafx.stage.Screen.getPrimary().visualBounds
internal val HEIGHT = com.rhs.murphyTCG.BOUNDS.height
internal val WIDTH = com.rhs.murphyTCG.BOUNDS.width

//Extension functions
internal operator fun javafx.scene.Parent.plus(node: javafx.scene.Node) = javafx.scene.Group(this.childrenUnmodifiable + node)
internal fun Array<com.rhs.murphyTCG.logic.CardWrapper?>.setFirstOpen(cw: com.rhs.murphyTCG.logic.CardWrapper) {
    val index = indexOfFirst { it == null }
    if(index < this.size) this[index] = cw
}

internal fun <T> T.but(f: (T) -> Unit): T {
    f(this)
    return this
}
internal operator fun javafx.scene.Parent.get(i: Int) = childrenUnmodifiable[i]
internal fun <E> stackOf(vararg es: E): java.util.Stack<E> {
    val result = java.util.Stack<E>()
    result.addAll(es)
    return result
}
internal operator fun com.rhs.murphyTCG.logic.Card.times(i: Int) = (1..i).map { this }

internal var goingFirst: Boolean? = null

fun DoNothing() = Unit
fun DoNothing(a1: Any) = Unit
fun DoNothing(a1: Any, a2: Any) = Unit
fun DoNothing(a1: Any, a2: Any, a3: Any) = Unit
fun DoNothing(a1: Any, a2: Any, a3: Any, a4: Any) = Unit

//TODO: Make these decks
internal val ServerDeck: java.util.Stack<com.rhs.murphyTCG.logic.Card> = com.rhs.murphyTCG.stackOf(*(
        com.rhs.murphyTCG.logic.Card.CHERUB * 3
                + com.rhs.murphyTCG.logic.Card.SERAPH * 4
                + com.rhs.murphyTCG.logic.Card.ARCHANGEL_MIKEY * 1
                + com.rhs.murphyTCG.logic.Card.DIVINE_WRATH * 2
        ).toTypedArray())
internal val ClientDeck: java.util.Stack<com.rhs.murphyTCG.logic.Card> = com.rhs.murphyTCG.stackOf(*(
        com.rhs.murphyTCG.logic.Card.IMP * 3
        ).toTypedArray())
internal var selectedDeck: java.util.Stack<com.rhs.murphyTCG.logic.Card> = com.rhs.murphyTCG.ServerDeck

internal var name: String = ""
internal var IP: InetAddress = InetAddress.getLocalHost()
internal var PORT = 30725

val console = Scanner(System.`in`)
val logger by lazy<(String) -> Unit> {
    val player = if(goingFirst!!) "Summoner" else "Receiver"
    return@lazy { message ->
        println("$player: $message")
        //console.next()
    }
}