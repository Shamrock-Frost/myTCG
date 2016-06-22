package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.*
import com.rhs.murphyTCG.network.Start
import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.GUI.HiddenCardNode
import com.rhs.murphyTCG.logic.BattleController
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Player
import java.util.Collections.shuffle
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.InetAddress
import java.util.*
import com.rhs.murphyTCG.logic.Card.Companion.Effect.*
import javafx.event.ActionEvent

//To Future Me: Objects are created lazily, don't worry
object Network {
    internal fun register() {
        val kryo = endPoint.kryo
        kryo.register(PhaseChange::class.java)
        kryo.register(Start::class.java)
        kryo.register(Stack::class.java)
        kryo.register(Chat::class.java)
        kryo.register(Attack::class.java)
        kryo.register(Pair::class.java)
        kryo.register(Draw::class.java)
        kryo.register(Hypothetical::class.java)
        kryo.register(Card.Companion.Effect::class.java)
        kryo.register(Summon::class.java)
        kryo.register(Card::class.java)
        kryo.register(Mana::class.java)
        kryo.register(NoResponse::class.java)
        kryo.register(Packet::class.java)
        kryo.register(EndTurn::class.java)
    }

    internal lateinit var endPoint: EndPoint

    internal fun send(packet: Any) {
        val ep = endPoint
        if(ep is Server) ep.sendToAllTCP(packet)
        else if(ep is Client) ep.sendTCP(packet)
    }

    internal fun init(root: VBox, controller: BattleController, isServer: Boolean): String {
        goingFirst = isServer
        if(isServer) endPoint = Server()
        else endPoint = Client()

        val ep = endPoint

        register()

        ep.addListener(object : Listener() {
            override fun connected(connection: Connection) =
                    send(Start().but {
                        it.name = name
                        it.deck = selectedDeck.but { shuffle(it) }
                    })

            override fun received(connection: Connection, packet: Any) {
                when(packet) {
                    is Start -> Platform.runLater {
                        controller.OppName.text = packet.name
                        controller.SelfName.text = name
                        controller.initialize(packet.deck)
                        controller.main.window.scene = Scene(root)
                    }
                    is Chat -> Platform.runLater {
                        controller.OppSays.text = packet.message
                    }
                    is Summon -> Platform.runLater {
                        //TODO: Allow response
                        val hidden = controller.OppHand.children.removeAt(packet.handIndex!!) as HiddenCardNode
                        val isMons = hidden.hiding.representing.wrapping.cardType === Card.Companion.CardType.MONSTER
                        logger("Playing ${hidden.hiding.representing} at board index ${packet.boardIndex}, from hand index ${packet.handIndex} visually")
                        ((
                            if(isMons) controller.OppMonsters
                            else controller.OppCastables
                        ).children[packet.boardIndex!!] as StackPane).children += if(!packet.hiding!!) hidden.hiding else hidden
                        val opp = controller.match.representing.player2
                        opp.currMana -= hidden.hiding.representing.wrapping.cost
                        logger("About to play ${hidden.hiding.representing} in game")
                        opp.play(opp.hand[packet.handIndex!!], packet.hiding!!, packet.boardIndex!!)
                        send(NoResponse().but { it.stack = packet.stack })
                    }
                    is PhaseChange -> Platform.runLater {
                        controller.incPhase()
                    }
                    is Draw -> Platform.runLater {
                        controller.OppHand.children += controller.OppDeck.children.removeAt(0)
                    }
                    is Hypothetical -> {
                        when(packet.action) {
                            CAST -> {
                                //TODO: Allow for response
                                val opp = controller.match.representing.player2
                                val board = if(packet.activatorIndex!! < 5) opp.monsters
                                            else opp.castables
                                packet.activatorIndex = packet.activatorIndex!!.mod(5)
                                while(board[packet.activatorIndex!!] == null);
                                val card = board[packet.activatorIndex!!] as CardWrapper
                                card.wrapping.onCast(card.context, card)
                                send(NoResponse())
                            }
                        }
                    }
                    is NoResponse -> {
                        //TODO: Unwrap the stack
                        while(!packet.stack.empty()) {
                            val resolving = packet.stack.pop()
                            when(resolving) {
                                is Hypothetical -> {
                                    when(resolving.action) {
                                        CAST -> {
                                            val player = if(resolving.playedBy!! == goingFirst) controller.match.representing.player1
                                                         else controller.match.representing.player2
                                            val card = (if(resolving.activatorIndex!! < 5) player.monsters[resolving.activatorIndex!!]
                                                        else player.castables[resolving.activatorIndex!! - 5]) as CardWrapper
                                            card.wrapping.onCast(card.context, card)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Mana -> {
                        controller.match.representing.player2.currMana = ++controller.match.representing.player2.mana
                        Platform.runLater {
                            controller.OppMana.text = "Mana: ${controller.match.representing.player2.currMana}"
                        }
                    }
                    is EndTurn -> {
                        controller.match.representing.endTurn()
                        Platform.runLater {
                            controller.NextPhase(null)
                        }
                    }
                }
            }

            override fun disconnected(connection: Connection?) = controller.main.close()
        })
        ep.start()

        if(ep is Server) ep.bind(PORT)
        else if(ep is Client) ep.connect(5000, IP, PORT)

        return InetAddress.getLocalHost().hostAddress
    }

    internal fun close() = endPoint.close()

    internal fun message(message: String) = send(Chat().but { it.message = message })
}