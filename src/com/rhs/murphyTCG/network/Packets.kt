package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.EndPoint
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
            value.push(this)
            stack = value
        }
}

internal class Start : Packet() {
    lateinit var name: String
    lateinit var deck: Stack<Card>
}

internal class Chat : Packet() {
    lateinit var message: String
}

internal class Attack : Packet() {
    var targetIndex: Int? = null
    var attackerIndex: Int? = null
}

internal class Draw : Packet()

internal class Hypothetical : Packet() {
    lateinit var action: Effect
    var activatorIndex: Int? = null
    //All targets will be chosen after confirmation
}

internal class Summon : Packet() {
    var handIndex: Int? = null
    var boardIndex: Int? = null
    var hiding: Boolean? = null
}

internal class NoResponse : Packet()

internal class PhaseChange