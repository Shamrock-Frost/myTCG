package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.logic.Match.Companion.Player

//All cards' inherit Card eventually
abstract class Card(val cardName: String,
                    val cost: Int,
                    val tribe: Tribe) {
    //Called on the card when the event triggers, but does nothing by default
    internal open fun toGrave() {}
    internal open fun onCast() {}
    internal open fun onDestroyed() {}
    internal open fun onDraw(drawnNormally: Boolean) {}
    internal open fun targeted(targetedBy: Card) {}
    internal open fun drawPhase() {}
    internal open fun standbyPhase() {}
    internal open fun battlePhase() {}
    internal open fun endPhase() {}
}

//Monsters can be instantiated in one line for vanillas
abstract class Monster(cardName: String,
                       cost: Int,
                       tribe: Tribe,
                       var attack: Int,
                       var health: Int
): Card(cardName, cost, tribe) {
    internal open fun onSummon() {}
    internal open fun onAttack(target: Monster) {}
    internal open fun onAttack(target: Player) {}
    internal open fun onAttacked(by: Monster) {}
    internal open fun onDamageChar(damaged: Monster) {}
    internal open fun onDamageChar(damaged: Player) {}
    internal open fun onDamaged(damagedBy: Card) {}
}

//These are just for typing/Structure, not inheritance
abstract class Castable(cardName: String, cost: Int, tribe: Tribe): Card(cardName, cost, tribe)
abstract class React(cardName: String, cost: Int, tribe: Tribe): Castable(cardName, cost, tribe)
abstract class Spell(cardName: String, cost: Int, tribe: Tribe): Castable(cardName, cost, tribe)

enum class Tribe() {
    ANGEL, DEMON, MAGIC, HUMAN, FAIRY, ORC
}

interface Hidden

class HiddenMonster(val hiding: Monster): Monster("blank", 0, Tribe.HUMAN, 0, 0), Hidden {
    override fun onAttacked(by: Monster) = throw IllegalStateException() /*Will turn up*/
}

class HiddenCastable(val hiding: Castable): Castable("blank", 0, Tribe.HUMAN), Hidden {
    //?????
}