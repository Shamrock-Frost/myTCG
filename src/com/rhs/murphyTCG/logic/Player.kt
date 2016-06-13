package com.rhs.murphyTCG.logic
import com.rhs.murphyTCG.firstOpen
import java.util.Stack
import java.util.ArrayList
import java.util.Collections.shuffle
import com.rhs.murphyTCG.logic.Card.Companion.CardType.*

internal class Player(val deck: Stack<CardWrapper>, hero: Card, context: Match) {
    val hero = CardWrapper(hero, context)
    val hand: ArrayList<CardWrapper> = ArrayList(MAX_HAND_SIZE)
    val monsters = arrayOfNulls<CardWrapper>(NUM_MONS)
    val castables = arrayOfNulls<CardWrapper>(NUM_CAST)
    val grave: ArrayList<CardWrapper> = ArrayList()
    var health = STARTING_HEALTH

    init {
        deck.forEach { it.context = context }
        shuffle(deck)
        draw(STARTING_HAND, false)
    }

    //Doesn't map because state can change on drawing
    fun draw(n: Int, drawnNormally: Boolean) {
        for (i in 1..n) {
            val card = deck.pop()
            card.wrapping.onDraw(true, card.context!!)
            hand += card
        }
    }

    //Moves a card from a player's hand to their board
    fun play(card: CardWrapper, hidden: Boolean) {
        hand.remove(card)
        card.hidden = hidden
        if (card.wrapping.cardType === MONSTER) monsters[monsters.firstOpen()] = card
        else castables[castables.firstOpen()] = card
    }

    private companion object {
        //Private to Player
        const val MAX_HAND_SIZE = 10
        const val STARTING_HAND = 5
        const val NUM_MONS = 5
        const val NUM_CAST = 5
        const val STARTING_HEALTH = 100
    }
}