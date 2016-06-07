package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.Card
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen
import java.net.InetAddress
import java.net.MulticastSocket

//Constants
internal val BOUNDS = Screen.getPrimary().visualBounds
internal val HEIGHT = BOUNDS.height
internal val WIDTH = BOUNDS.width

//Extension functions
internal operator fun Parent.plus(node: Node) = Group(this.childrenUnmodifiable + node)
internal fun Array<out Card?>.firstOpen() = this.indexOfFirst { it == null }
internal operator fun Parent.get(i: Int) = childrenUnmodifiable[i]

internal class InvalidCardTypeException : IllegalStateException("The card was neither Monster nor Castable")

//Networking
val LOCAL_HOST = InetAddress.getLocalHost()
val PORT: Int = 9090
val allChanells = {
    val temp = MulticastSocket(PORT + 1)
    temp.joinGroup(LOCAL_HOST)
    temp;
}()