package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.InvalidCardTypeException
import com.rhs.murphyTCG.firstOpen
import java.util.Stack
import java.util.ArrayList
import java.util.Collections.shuffle

//Player is the player in the game with hand and deck etc...
//Profile is what Users are called, which have deck recipes/login info
internal class Match(deck1: Stack<Card>, deck2: Stack<Card>) {
    internal val player1: Player = Player(deck1)
    internal val player2: Player = Player(deck2)
    private var currentPlayer = player1
    
    companion object {
        data class Player(val deck: Stack<Card>) {
            val hand: ArrayList<Card> = ArrayList(MAX_HAND_SIZE)
            val monsters = arrayOfNulls<Monster?>(NUM_MONS)
            val castables = arrayOfNulls<Castable?>(NUM_CAST)
            val grave: ArrayList<Card> = ArrayList()
            var health = STARTING_HEALTH

            init {
                shuffle(deck)
                draw(STARTING_HAND, false)
            }

            //Doesn't map because state can change on drawing
            fun draw(n: Int, drawnNormally: Boolean) {
                for(i in 1..n) {
                    val card = deck.pop()
                    card.onDraw(drawnNormally)
                    hand += card
                }
            }

            //Moves a card from a player's hand to their board
            fun play(card: Card, hidden: Boolean) {
                hand.remove(card)
                when(card) {
                    is Monster -> {
                        monsters[monsters.firstOpen()] =
                                if(hidden) card else HiddenMonster(card)
                    }
                    is Castable -> {
                        castables[castables.firstOpen()] =
                                if(hidden) card else HiddenCastable(card)
                    }
                    else -> throw InvalidCardTypeException()
                }
            }

            companion object {                
                //Private to Player
                private val MAX_HAND_SIZE = 10
                private val STARTING_HAND = 5
                private val NUM_MONS = 5
                private val NUM_CAST = 5
                private val STARTING_HEALTH = 100
            }
        }
    }

    //Methods called when phases are entered
    fun drawPhase() {
        currentPlayer.draw(1, true)
        currentPlayer.monsters.forEach { it?.drawPhase() }
        currentPlayer.castables.forEach { it?.drawPhase() }
    }

    fun standbyPhase() {
        currentPlayer.monsters.forEach { it?.standbyPhase() }
        currentPlayer.castables.forEach { it?.standbyPhase() }
    }

    fun battlePhase() {
        currentPlayer.monsters.forEach { it?.battlePhase() }
        currentPlayer.castables.forEach { it?.battlePhase() }
    }

    fun endPhase() {
        currentPlayer.monsters.forEach { it?.endPhase() }
        currentPlayer.castables.forEach { it?.endPhase() }
        currentPlayer = if(currentPlayer !== player1) player1 else player2
    }

    fun combat(attacker: Monster, defender: Monster) : List<Monster> {
        attacker.onAttack(defender)
        defender.onAttacked(attacker)
        attacker.health -= defender.attack
        defender.health -= attacker.attack
        attacker.onDamageChar(defender)
        defender.onDamaged(attacker)
        val dead = listOf(attacker, defender).filter { it.health <= 0 }
        dead.forEach { kill(it) }
        return dead
    }

    fun kill(monster: Monster) {
        val owner = if(monster in player1.monsters) player1 else player2
        owner.monsters[owner.monsters.indexOf(monster)] = null
        monster.onDestroyed()
        owner.grave += monster
    }

    //Will returns winner
    fun endMatch(): Unit = throw IllegalStateException() /*To be implemented*/
}