package com.rhs.murphyTCG.logic

internal open class CardWrapper(val wrapping: Card, var context: Match? = null) {
    var hidden: Boolean = false

    var health = wrapping.health
    var attack = wrapping.attack
    var mana = wrapping.mana

    fun kill(cw: CardWrapper) = context?.kill(cw, this)
}