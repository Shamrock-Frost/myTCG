package com.rhs.murphyTCG.network

import com.esotericsoftware.kryonet.EndPoint
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.CardWrapper
import com.rhs.murphyTCG.logic.Card.Companion.Effect
import java.util.Stack

internal data class Summoned(val card: CardWrapper)
internal data class Activated(val card: CardWrapper, val effect: Effect)
internal data class Attack(val attacker: CardWrapper, val defender: CardWrapper)

internal class Start() {
    var name: String? = null
}
internal data class NoResponse(val no: Unit)
internal class SayHi() {
    var message: String? = null
}

internal fun register(ep: EndPoint) {
    val kryo = ep.kryo
    kryo.register(Start::class.java)
    //kryo.register(Activated::class.java)
    //kryo.register(Summoned::class.java)
    //kryo.register(Attack::class.java)
    //kryo.register(Start::class.java)
    //kryo.register(NoResponse::class.java)
    kryo.register(SayHi::class.java)
}