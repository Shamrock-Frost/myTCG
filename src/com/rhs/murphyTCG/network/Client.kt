package com.rhs.murphyTCG.network

import com.rhs.murphyTCG.PORT
import com.rhs.murphyTCG.network.Start
import com.rhs.murphyTCG.isServer
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.logic.BattleController
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Player
import java.util.Stack

//To Future Me: Objects are created lazily, don't worry
object Client {
    val client = Client()

    internal fun init(main: AppMain, player: Card, controller: BattleController, deck: Stack<CardWrapper>) {
        isServer = false

        register(client)
        client.start()

        client.addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                client.sendTCP(Start(player, deck))
            }

            override fun received(connection: Connection?, packet: Any?) {
                when(packet) {
                    is Start -> {
                        controller.initMatch(packet.deck, packet.hero)
                        //TODO: Start match
                    }

                    is Activated -> {
                        //TODO: Handle a player's move
                    }

                    is Summoned -> {
                        //TODO: Handle a player's summon
                    }
                }
            }

            override fun disconnected(connection: Connection?) = main.close()
        })

    }
}