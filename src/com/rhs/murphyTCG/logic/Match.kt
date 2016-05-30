package com.rhs.murphyTCG.logic

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
            fun play(index: Int, hidden: Boolean) {
                val toPlay = hand.removeAt(index)
                when(toPlay) {
                    is Monster -> {
                        monsters[monsters.firstOpen()] =
                                if(hidden) toPlay else hiddenMonster(toPlay)
                    }
                    is Castable -> {
                        castables[castables.firstOpen()] =
                                if(hidden) toPlay else hiddenCastable(toPlay)
                    }
                    else -> throw IllegalStateException("Card was neither Monster nor Castable")
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

    //Will returns winner
    fun endMatch(): Unit = throw IllegalStateException() /*To be implemented*/
}