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
        val MAX_HAND_SIZE = 10
        val STARTING_HAND = 5
        val NUM_MONS = 5
        val NUM_CAST = 5
        val STARTING_HEALTH = 100

        private class Player(val deck: Stack<Card>) {
            val hand = ArrayList<Card>(MAX_HAND_SIZE)
            val frontRow = arrayOfNulls<Monster?>(NUM_MONS)
            val backRow = arrayOfNulls<Castable?>(NUM_CAST)
            val grave = ArrayList<Card>()
            var health = STARTING_HEALTH

            init {
                shuffle()
                draw(STARTING_HAND)
            }

            fun shuffle() = Collections.shuffle(deck)

            fun draw(n: Int) = {
                val tempList = (1..n).map { deck.pop() }
                tempList.forEach { it?.onDraw(true) }
                hand.addAll(tempList)
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
        }
    }

    //Methods called when phases are entered
    fun drawPhase() {
        currentPlayer.draw(1)
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