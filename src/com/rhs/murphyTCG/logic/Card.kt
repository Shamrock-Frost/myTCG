package com.rhs.murphyTCG.logic

//All cards' inherit Card eventually
abstract class Card(val cardName: String,
                    val cost: Int,
                    val tribe: Tribe) {
    //Called on the card when the event triggers, but does nothing by default
    open fun toGrave() {}
    open fun onCast() {}
    open fun onDestroyed() {}
    open fun onDraw(drawnNormally: Boolean) {}
    open fun targeted(targetedBy: Card) {}
    open fun drawPhase() {}
    open fun standbyPhase() {}
    open fun battlePhase() {}
    open fun endPhase() {}

    final fun hidden(): Hidden = when(this) {
        is Monster -> hiddenMonster(this)
        is Castable -> hiddenCastable(this)
        else -> throw IllegalStateException("Card was neither Monster nor Castable")
    }
}

//Monsters can be instantiated in one line for vanillas
abstract class Monster(cardName: String,
                       cost: Int,
                       tribe: Tribe,
                       val attack: Int,
                       val health: Int
): Card(cardName, cost, tribe) {
    open fun onSummon() {}
    open fun onAttack(target: Card) {}
    open fun onAttacked(target: Card) {}
    open fun onDamageChar(damaged: Card) {}
    open fun onDamaged(damagedBy: Card) {}
}

//These are just for typing/Structure, not inheritance
abstract class Castable(cardName: String, cost: Int, tribe: Tribe): Card(cardName, cost, tribe)
abstract class React(cardName: String, cost: Int, tribe: Tribe): Castable(cardName, cost, tribe)
abstract class Spell(cardName: String, cost: Int, tribe: Tribe): Castable(cardName, cost, tribe)

enum class Tribe() {
    ANGEL, DEMON, MAGIC, HUMAN, FAIRY, ORC
}

interface Hidden

class hiddenMonster(val hiding: Monster): Monster("blank", 0, Tribe.HUMAN, 0, 0), Hidden {
    override fun onAttacked(target: Card) = throw IllegalStateException() /*Will turn up*/
}

class hiddenCastable(val hiding: Castable): Castable("blank", 0, Tribe.HUMAN), Hidden {
    //?????
}