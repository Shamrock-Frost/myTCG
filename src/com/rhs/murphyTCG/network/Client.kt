package com.rhs.murphyTCG.network

import com.rhs.murphyTCG.network.Start
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.logic.BattleController
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Player
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import java.net.InetAddress
import java.util.Stack

//To Future Me: Objects are created lazily, don't worry
object Client {
    val client = Client()

    internal fun init(root: VBox, controller: BattleController) {
        isServer = false

        register(client)
        client.start()

        client.addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                client.sendTCP(Start().but { it.name = name })
            }

            override fun received(connection: Connection?, packet: Any?) {
                println("received")
                when(packet) {
                    is Start -> {
                        controller.OppName.text = packet.name
                        controller.SelfName.text = name
                        Platform.runLater {
                            controller.initialize()
                            controller.main.window.scene = Scene(root)
                        }
                    }
                    is SayHi -> Platform.runLater {
                        controller.OppSays.text = packet.message
                    }
                }
            }

            override fun disconnected(connection: Connection?) = controller.main.close()
        })

        client.connect(5000, IP, PORT)
    }

    internal fun send(message: String) = client.sendTCP(SayHi().but { it.message = message })
}