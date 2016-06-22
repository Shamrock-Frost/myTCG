package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.EndPoint
import com.rhs.murphyTCG.but
import com.rhs.murphyTCG.goingFirst
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Card.Companion.Effect
import com.rhs.murphyTCG.logic.Match
import com.rhs.murphyTCG.logic.Player
import java.util.*

abstract class Packet {
    val playedBy = goingFirst //True means server, false is client
    var stack = Stack<Packet>()
        set(value) {
            value.push(copy())
            field = value
        }
    abstract fun copy(): Packet
}

internal class Start : Packet() {
    lateinit var name: String
    lateinit var deck: Stack<Card>
    override fun copy() = Start().but {
        it.name = name
        it.deck = deck
    }
}

internal class Chat : Packet() {
    lateinit var message: String
    override fun copy() = Chat().but { it.message = message }
}

internal class Attack : Packet() {
    var targetIndex: Int? = null
    var attackerIndex: Int? = null

    override fun copy() = Attack().but {
        it.targetIndex = targetIndex
        it.attackerIndex = attackerIndex
    }
}

internal class Draw : Packet() {
    override fun copy() = Draw()
}

internal class Mana

internal class Hypothetical : Packet() {
    lateinit var action: Effect
    var activatorIndex: Int? = null
    override fun copy() = Hypothetical().but {
        it.action = action
        it.activatorIndex = activatorIndex
    }
}

internal class Summon : Packet() {
    var handIndex: Int? = null
    var boardIndex: Int? = null
    var hiding: Boolean? = null

    override fun copy() = Summon().but {
        it.handIndex = handIndex
        it.boardIndex = boardIndex
        it.hiding = hiding
    }
}

internal class EndTurn() : Packet() {
    override fun copy() = EndTurn()
}

internal class NoResponse : Packet() {
    override fun copy() = NoResponse()
}

internal class PhaseChange