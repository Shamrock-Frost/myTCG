package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.EndPoint
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Card.Companion.Effect
import java.util.Stack

abstract class Packet {
    var stack = Stack<Packet>()
}

internal class Start : Packet() {
    lateinit var name: String
    lateinit var deck: Stack<Card>
}

internal class Chat : Packet() {
    lateinit var message: String
}

internal class Attack : Packet() {
    lateinit var target: CardWrapper
    lateinit var attacker: CardWrapper
}

internal class Draw : Packet()

internal class Hypothetical : Packet() {
    lateinit var action: Effect
    lateinit var activator: CardWrapper
    //All targets will be chosen after confirmation
}

internal class Summon : Packet() {
    lateinit var mons: CardWrapper
}

internal fun register(ep: EndPoint) {
    val kryo = ep.kryo
    kryo.register(Stack::class.java)
    kryo.register(Start::class.java)
    kryo.register(Hypothetical::class.java)
    kryo.register(Summon::class.java)
    kryo.register(Chat::class.java)
    kryo.register(Draw::class.java)
    kryo.register(Attack::class.java)
}