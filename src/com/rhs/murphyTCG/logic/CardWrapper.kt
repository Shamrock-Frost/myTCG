package com.rhs.murphyTCG.logic

internal open class CardWrapper(val wrapping: Card, val context: Match) {
    var hidden: Boolean = false

    var health = wrapping.health
    var attack = wrapping.attack
    var mana = wrapping.mana

    fun kill(cw: CardWrapper) = context.kill(cw, this)

    override fun toString() =  wrapping.name
}