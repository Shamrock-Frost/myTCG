package com.rhs.murphyTCG.logic

import java.util.Stack
import java.util.ArrayList
import java.util.Collections

//Player is the player in the game with hand and deck etc...
//Profile is what Users are called, which have deck recipes/login info
class Match(deck1: Stack<Card>, deck2: Stack<Card>) {
    private val player1: Player = Player(deck1)
    private val player2: Player = Player(deck2)
    private var currentPlayer = player1

    companion object {
        //Should only be accessed from Match
        private class Player(val deck: Stack<Card>) {
            val hand = ArrayList<Card>(MAX_HAND_SIZE)
            val frontRow = arrayOfNulls<Monster?>(NUM_MONS)
            val backRow = arrayOfNulls<Castable?>(NUM_CAST)
            val grave = ArrayList<Card>()
            var health = STARTING_HEALTH

            init {
                deck.shuffle()
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
                //Works with kotlin typing, don't try to fix
                when(toPlay) {
                    is Monster -> frontRow[frontRow.indexOfLast { it == null }] =
                            if(hidden) toPlay else hiddenMonster(toPlay)
                    is Castable -> backRow[backRow.indexOfLast { it == null }] =
                            if(hidden) toPlay else hiddenCastable(toPlay)
                }
            }

            companion object {
                //Private to Player
                private val MAX_HAND_SIZE = 10
                private val STARTING_HAND = 5
                private val NUM_MONS = 5
                private val NUM_CAST = 5
                private val STARTING_HEALTH = 100
                private fun Stack<Card>.shuffle() = Collections.shuffle(this)
            }
        }
    }

    //Methods called when phases are entered
    fun drawPhase() {
        currentPlayer.draw(1, true)
        currentPlayer.frontRow.forEach { it?.drawPhase() }
        currentPlayer.backRow.forEach { it?.drawPhase() }
    }

    fun standbyPhase() {
        currentPlayer.frontRow.forEach { it?.standbyPhase() }
        currentPlayer.backRow.forEach { it?.standbyPhase() }
    }

    fun battlePhase() {
        currentPlayer.frontRow.forEach { it?.battlePhase() }
        currentPlayer.backRow.forEach { it?.battlePhase() }
    }

    fun endPhase() {
        currentPlayer.frontRow.forEach { it?.endPhase() }
        currentPlayer.backRow.forEach { it?.endPhase() }
        currentPlayer = if(currentPlayer !== player1) player1 else player2
    }

    //Will returns winner
    fun endMatch(): Unit = throw IllegalStateException() /*To be implemented*/
}