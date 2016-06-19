package com.rhs.murphyTCG.logic
import com.rhs.murphyTCG.goingFirst
import java.util.Stack

//Player is the player in the game with hand and deck etc...
//Profile is what Users are called, which have deck recipes/login info
internal class Match(deck1: Stack<Card>, hero1: Card, deck2: Stack<Card>, hero2: Card) {
    internal var phase = 0

    internal val player1: Player = Player(deck1, hero1, this)
    internal val player2: Player = Player(deck2, hero2, this)
    private var currentPlayer = if(goingFirst!!) player1 else player2

    //Methods called when phases are entered
    fun drawPhase() {
        currentPlayer.draw(1, true)
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.drawPhase(it.context, it) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.drawPhase(it.context, it) }
    }

    fun standbyPhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.standbyPhase(it.context, it) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.standbyPhase(it.context, it) }
    }

    fun battlePhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.battlePhase(it.context, it) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.battlePhase(it.context, it) }
    }

    fun endPhase() {
        currentPlayer.monsters.filterNotNull().forEach { it.wrapping.endPhase(it.context, it) }
        currentPlayer.castables.filterNotNull().forEach { it.wrapping.endPhase(it.context, it) }
        currentPlayer = if(currentPlayer !== player1) player1 else player2
    }

    fun combat(attacker: CardWrapper, defender: CardWrapper) : List<CardWrapper> {
        attacker.wrapping.onAttack(defender, attacker.context, attacker)
        defender.wrapping.onAttacked(attacker, defender.context, defender)
        attacker.health = attacker.health!!.minus(defender.wrapping.attack as Int)
        defender.health = defender.health!!.minus(attacker.wrapping.attack as Int)
        attacker.wrapping.onDamageChar(defender, attacker.context, attacker)
        defender.wrapping.onDamaged(attacker, attacker.context, defender)
        val dead = listOf(attacker, defender).filter { (it.wrapping.health as Int) <= 0 }
        if(attacker in dead) kill(attacker, defender)
        if(defender in dead) kill(defender, attacker)
        return dead
    }

    fun kill(monster: CardWrapper, killer: CardWrapper) {
        val owner = if(monster in player1.monsters) player1 else player2
        owner.monsters[owner.monsters.indexOf(monster)] = null
        monster.wrapping.onDestroyed(killer, this, monster)
        owner.grave += monster
    }

    //Will returns winner
    fun endMatch(): Unit = throw IllegalStateException() //TODO: End the game

    val yourTurn: Boolean
        get() = currentPlayer == player1
}