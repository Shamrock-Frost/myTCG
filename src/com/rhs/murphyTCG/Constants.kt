package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.Card
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.stage.Screen
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.ServerSocket

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
val PORT: Int = ServerSocket(0).localPort

//Global state is bad and I'm a bad programmer
//Nullable global state makes me feel dead inside
var isServer: Boolean? = null