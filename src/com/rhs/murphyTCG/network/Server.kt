package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.logic.BattleController
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.Card.Companion.Effect
import com.rhs.murphyTCG.logic.Card.Companion.EffectFunction
import java.util.Stack
import com.rhs.murphyTCG.logic.CardWrapper
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox

//To Future Me: Objects are created lazily, don't worry
object Server {
    var noResponse = true

    val server = Server()

    internal fun init(root: VBox, controller: BattleController) {
        isServer = true

        register(server)
        server.addListener(object : Listener() {
            override fun connected(connection: Connection) {
                connection.sendTCP(Start().but { it.name = name })
            }

            override fun received(connection: Connection?, packet: Any?) {
                println("received")
                when (packet) {
                    is Start -> {
                        controller.OppName.text = packet.name
                        controller.SelfName.text = name
                        Platform.runLater {
                            controller.main.window.scene = Scene(root)
                        }
                    }
                    is SayHi -> {
                        Platform.runLater {
                            controller.OppGrave.children.clear()
                            controller.OppGrave.children.add(Label(packet.message))
                        }
                    }
                }
                noResponse = false
            }

            override fun disconnected(c: Connection?) = controller.main.close()
        })

        server.bind(PORT)
        server.start()
    }

    internal fun send(message: String) = server.sendToAllTCP(SayHi().but { it.message = message })
}