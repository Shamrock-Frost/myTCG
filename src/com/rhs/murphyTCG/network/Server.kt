package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.PORT
import com.rhs.murphyTCG.isServer
import com.rhs.murphyTCG.logic.BattleController
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.Card.Companion.Effect
import com.rhs.murphyTCG.logic.Card.Companion.EffectFunction
import java.util.Stack
import com.rhs.murphyTCG.logic.CardWrapper

//To Future Me: Objects are created lazily, don't worry
object Server {
    var noResponse = true

    val server = object : Server() {
        override fun newConnection() = TCGConnection()
    }

    internal fun init(main: AppMain, player: Card, controller: BattleController, deck: Stack<CardWrapper>) {
        isServer = true

        register(server)
        server.addListener(object : Listener() {
            override fun connected(connection: Connection) {
                connection.sendTCP(Start(player, deck))
            }

            override fun received(connection: Connection?, packet: Any?) {
                if(connection !is TCGConnection) throw IllegalStateException("Server's connection was null")
                when(packet) {
                    is Start -> {
                        controller.initMatch(packet.deck, packet.hero)
                        //TODO: Start match
                    }

                    is Activated -> {
                        val effect = packet.card.wrapping.effects.get(packet.effect)
                        //when()
                    }

                    is Summoned -> {
                        //TODO: Handle a player's summon
                    }
                }
                noResponse = false
            }

            override fun disconnected(c: Connection?) = main.close()
        })

        server.bind(PORT)
        server.start()
    }

    private class TCGConnection : Connection() {
        internal var p1Name: String? = null
        internal var p2Name: String? = null
        //TODO: Add match info stuff.
    }
}