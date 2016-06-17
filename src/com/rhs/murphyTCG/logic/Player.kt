package com.rhs.murphyTCG.logic
import com.rhs.murphyTCG.but
import java.util.Stack
import java.util.ArrayList
import java.util.Collections.shuffle
import com.rhs.murphyTCG.logic.Card.Companion.CardType.*
import com.rhs.murphyTCG.setFirstOpen

internal class Player(deck: Stack<Card>, hero: Card, context: Match) {
    val deck = Stack<CardWrapper>()
    val hero = CardWrapper(hero, context)
    val hand: ArrayList<CardWrapper> = ArrayList(MAX_HAND_SIZE)
    val monsters = arrayOfNulls<CardWrapper>(NUM_MONS)
    val castables = arrayOfNulls<CardWrapper>(NUM_CAST)
    val grave: ArrayList<CardWrapper> = ArrayList()
    var health: Int
        get() = hero.health as Int
        set(value) { hero.health = value }
    var mana: Int
        get() = hero.mana as Int
        set(value) { hero.mana = value }

    init {
        this.deck.addAll(deck.map { CardWrapper(it, context) })
        shuffle(deck)
        draw(STARTING_HAND, false)
    }

    //Doesn't map because state can change on drawing
    fun draw(n: Int, drawnNormally: Boolean) {
        for (i in 1..n) {
            val card = deck.pop()
            card.wrapping.onDraw(true, card.context, card)
            hand += card
        }
    }

    //Moves a card from a player's hand to their board
    fun play(card: CardWrapper, hidden: Boolean) {
        hand.remove(card)
        card.hidden = hidden
        if (card.wrapping.cardType === MONSTER) monsters.setFirstOpen(card)
        else castables.setFirstOpen(card)
    }

    fun mill(n: Int) = grave.addAll((1..n).map { deck.pop() })

    private companion object {
        //Private to Player
        const val MAX_HAND_SIZE = 10
        const val STARTING_HAND = 5
        const val NUM_MONS = 5
        const val NUM_CAST = 5
    }
}