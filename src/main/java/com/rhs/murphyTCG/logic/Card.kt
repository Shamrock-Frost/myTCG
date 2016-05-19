package com.rhs.murphyTCG.logic

interface Card {
    val cardName: String
    val cost: Int
    val tribe: Tribe

    //Called on the card when the event triggers, but does nothing by default
    fun toGrave() {}
    fun onCast() {}
    fun onDestroyed() {}
    fun onDraw(drawnNormally: Boolean) {}
    fun targeted(targetedBy: Card) {}
    fun drawPhase() {}
    fun standbyPhase() {}
    fun battlePhase() {}
    fun endPhase() {}
}

abstract class Monster(override val cardName: String,
                       override val cost: Int,
                       override val tribe: Tribe,
                       val attack: Int,
                       val health: Int
): Card {
    open fun onSummon() {}
    open fun onAttack(target: Card) {}
    open fun onAttacked(target: Card) {}
    open fun onDamageChar(damaged: Card) {}
    open fun onDamaged(damagedBy: Card) {}
}

interface Castable: Card
abstract class Spell(override val cardName: String,
                     override val cost: Int,
                     override val tribe: Tribe
): Castable
abstract class React(override val cardName: String,
                     override val cost: Int,
                     override val tribe: Tribe
): Castable

enum class Tribe() {
    ANGEL, DEMON, MAGIC, HUMAN, FAIRY, ORC
}

class hiddenMonster(val hiding: Monster): Monster("blank", 0, Tribe.HUMAN, 0, 0) {
    override fun onAttacked(target: Card) = throw IllegalStateException() /*Will turn up*/
}

class hiddenCastable(val hiding: Castable): Castable {
    override val cardName = "blank"
    override val cost = 0
    override val tribe = Tribe.HUMAN
    /*???*/
}