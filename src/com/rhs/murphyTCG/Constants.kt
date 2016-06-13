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
internal fun Array<CardWrapper?>.setFirstOpen(cw: CardWrapper) {
    val index = indexOfFirst { it == null }
    if(index < this.size) this[index] = cw
}
internal operator fun Parent.get(i: Int) = childrenUnmodifiable[i]
internal fun <T> T.but(f: (T) -> Unit): T {
    f(this)
    return this
}
internal fun <E> stackOf(vararg es: E): Stack<E> {
    val result = Stack<E>()
    result.addAll(es)
    return result
}
internal operator fun Int.plus(cw: CardWrapper) = cw.attack!!.plus(this)

const val PORT = 30725
val localhost = InetAddress.getLocalHost()

//Global state is bad and I'm a bad programmer
//Nullable global state makes me feel dead inside
var isServer: Boolean? = null

fun DoNothing() = Unit
fun DoNothing(a1: Any) = Unit
fun DoNothing(a1: Any, a2: Any) = Unit
fun DoNothing(a1: Any, a2: Any, a3: Any) = Unit
fun DoNothing(a1: Any, a2: Any, a3: Any, a4: Any) = Unit

//TODO: Make these decks
internal val ServerDeck: Stack<CardWrapper> = stackOf(
    CardWrapper(Card.CHERUB)
    , CardWrapper(Card.CHERUB)
    , CardWrapper(Card.CHERUB)
    , CardWrapper(Card.SERAPH)
    , CardWrapper(Card.SERAPH)
    , CardWrapper(Card.SERAPH)
    , CardWrapper(Card.SERAPH)
    , CardWrapper(Card.ARCHANGEL_MIKEY)
    , CardWrapper(Card.DIVINE_WRATH)
    , CardWrapper(Card.DIVINE_WRATH)
)
internal val ClientDeck: Stack<CardWrapper> = stackOf()

internal var name: String? = null