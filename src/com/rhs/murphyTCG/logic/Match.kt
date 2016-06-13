package com.rhs.murphyTCG.logic
import java.util.Stack

//Player is the player in the game with hand and deck etc...
//Profile is what Users are called, which have deck recipes/login info
internal class Match(deck1: Stack<CardWrapper>, hero1: Card, deck2: Stack<CardWrapper>, hero2: Card) {
    internal val player1: Player = Player(deck1, hero1, this)
    internal val player2: Player = Player(deck2, hero2, this)
    private var currentPlayer = player1

    //Methods called when phases are entered
    fun drawPhase() {
        currentPlayer.draw(1, true)
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.drawPhase(it.context!!) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.drawPhase(it.context!!) }
    }

    fun standbyPhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.standbyPhase(it.context!!) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.standbyPhase(it.context!!) }
    }

    fun battlePhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.battlePhase(it.context!!) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.battlePhase(it.context!!) }
    }

    fun endPhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.endPhase(it.context!!) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.endPhase(it.context!!) }
        currentPlayer = if(currentPlayer !== player1) player1 else player2
    }

    fun combat(attacker: CardWrapper, defender: CardWrapper) : List<CardWrapper> {
        attacker.wrapping.onAttack(defender, attacker.context!!)
        defender.wrapping.onAttacked(attacker, defender.context!!)
        attacker.health = attacker.health!!.minus(defender.wrapping.attack as Int)
        defender.health = defender.health!!.minus(attacker.wrapping.attack as Int)
        attacker.wrapping.onDamageChar(defender, attacker.context!!)
        defender.wrapping.onDamaged(attacker, attacker.context!!)
        val dead = listOf(attacker, defender).filter { (it.wrapping.health as Int) <= 0 }
        dead.forEach { kill(it) }
        return dead
    }

    fun kill(monster: CardWrapper) {
        val owner = if(monster in player1.monsters) player1 else player2
        owner.monsters[owner.monsters.indexOf(monster)] = null
        monster.wrapping.onDestroyed(this)
        owner.grave += monster
    }

    //Will returns winner
    fun endMatch(): Unit = throw IllegalStateException() //TODO: End the game
}