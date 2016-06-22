package com.rhs.murphyTCG.logic
import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.logic.Card.Companion.CardType.*
import com.rhs.murphyTCG.network.Hypothetical
import java.util.*

internal class Player(deck: Stack<Card>, hero: Card, val context: Match) {
    val deck = Stack<CardWrapper>()
    val hero = CardWrapper(hero, context)
    val hand: MutableList<CardWrapper> = ArrayList(MAX_HAND_SIZE)
    val monsters = arrayOfNulls<CardWrapper>(NUM_MONS)
    val castables = arrayOfNulls<CardWrapper>(NUM_CAST)
    val grave: MutableList<CardWrapper> = ArrayList()
    var health: Int
        get() = hero.health!!
        set(value) { hero.health = value }
    var currMana: Int = hero.mana!!
    var mana: Int
        get() = hero.mana!!
        set(value) {
            hero.mana = value
        }

    init {
        this.deck.addAll(deck.map { CardWrapper(it, context) })
        draw(STARTING_HAND, false)
    }

    //Doesn't map because state can change on drawing
    fun draw(n: Int, drawnNormally: Boolean) {
        for (i in 1..n) {
            val card = try {
                deck.pop()
            } catch (e: EmptyStackException) {
                this.context.endMatch(this)
                throw IllegalStateException("Match did not end")
            }
            card.wrapping.onDraw(drawnNormally, card.context, card)
            hand += card
        }
    }

    //Moves a card from a player's hand to their board
    fun play(card: CardWrapper, hidden: Boolean, index: Int) {
        currMana -= card.wrapping.cost
        logger("Playing $card from ${hand.indexOf(card)}")
        hand -= card
        card.hidden = hidden
        if (card.wrapping.cardType === MONSTER) monsters[index] = card
        else castables[index] = card
        logger("Played on board spot #$index")
    }

    fun mill(n: Int) = grave.addAll((1..Math.min(n, deck.size)).map { deck.pop() })

    private companion object {
        //Private to Player
        const val MAX_HAND_SIZE = 10
        const val STARTING_HAND = 5
        const val NUM_MONS = 5
        const val NUM_CAST = 5
    }
}