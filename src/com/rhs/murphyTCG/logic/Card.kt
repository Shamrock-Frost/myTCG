package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.DoNothing
import com.rhs.murphyTCG.but
import com.rhs.murphyTCG.plus
import com.rhs.murphyTCG.setFirstOpen
import java.util.*

internal enum class Card(val cardName: String,
                         val cost: Int,
                         val tribe: Tribe,
                         val cardType: CardType,
                         val attack: Int? = null,
                         val health: Int? = null,
                         val mana: Int? = null,
                         val toGrave: (Match, CardWrapper) -> Unit = ::DoNothing,
                         val onCast: (Match, CardWrapper) -> Unit = ::DoNothing,
                         val onDestroyed: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val onDraw: (Boolean, Match, CardWrapper) -> Unit = ::DoNothing,
                         val drawPhase: (Match, CardWrapper) -> Unit = ::DoNothing,
                         val standbyPhase: (Match, CardWrapper) -> Unit = ::DoNothing,
                         val battlePhase: (Match, CardWrapper) -> Unit = ::DoNothing,
                         val endPhase: (Match, CardWrapper) -> Unit = ::DoNothing,
//                         val targeted: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val onAttack: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val onAttacked: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val onDamageChar: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val onDamaged: (CardWrapper, Match, CardWrapper) -> Unit = ::DoNothing,
                         val reactTo: (CardWrapper, Effect, Match, CardWrapper) -> Unit = ::DoNothing
) {
    Test(cardName = "test", cost = 0, tribe = Tribe.HUMAN, cardType = CardType.MONSTER)
    , BadDude(cardName = "Rubicante the Rabid", cost = 0, tribe = Tribe.DEMON, cardType = CardType.HERO, health = 50, mana = 3)
    , GoodGirl(cardName = "Kushiel the Arbiter", cost = 0, tribe = Tribe.ANGEL, cardType = CardType.HERO, health = 70, mana = 0)
    , CHERUB(cardName = "Cherub", cost = 1, tribe = Tribe.ANGEL, cardType = CardType.MONSTER, health = 1, attack = 3, onDestroyed = { by, match, cw ->
        match.player1.draw(2, false)
        match.player1.health -= 4
    })
    , SERAPH(cardName = "Seraph", cost = 3, tribe = Tribe.ANGEL, cardType = CardType.MONSTER, health = 4, attack = 0, standbyPhase = { match, cw ->
        match.player1.monsters.setFirstOpen(CardWrapper(CHERUB, match))
        match.player2.monsters.setFirstOpen(CardWrapper(IMP, match))
    })
    , ARCHANGEL_MIKEY(cardName = "Michael, Archangel of Wrath", cost = 8, tribe = Tribe.ANGEL, cardType = CardType.MONSTER, health = 9, attack = 3,
            onCast = { match, cw ->
                val selfMons = match.player1.monsters.filterNotNull()
                selfMons.forEach { cw.kill(it) }
                val oppMons = match.player2.monsters.filterNotNull()
                oppMons.forEach { cw.kill(it) }

                val totalATK = selfMons.fold(0, { curr, cw -> curr + cw }) + selfMons.fold(0, { curr, cw -> curr + cw })
                match.player1.health -= totalATK / 2
                cw.attack = cw.attack!!.plus(totalATK / 4)
            }
    )
    , DIVINE_WRATH(cardName = "Divine Wrath", cost = 2, tribe = Tribe.ANGEL, cardType = CardType.REACT, reactTo = { reactTo, effUsed, match, cw ->
        when(effUsed) {
            Effect.DEALTDMG -> {
                cw.kill(reactTo)
                match.player2.mill(reactTo.wrapping.cost)
            }
        }
    })
    , IMP(cardName = "Imp", cost = 1, tribe = Tribe.DEMON, cardType = CardType.MONSTER, health = 1, attack = 1, onDestroyed = { by, match, cw ->
        val mons = match.player2.monsters.filterNotNull()
        val chosen = mons[Random().nextInt(mons.size)]
        cw.kill(chosen)
        match.player2.health -= Math.round(chosen.attack!!.times(1.5)).toInt()
    })
    ,
    ;

    //For ease of sending stuff over the network
    val effects: EnumMap<Effect, EffectFunction> = EnumMap(mapOf(
        Effect.GRAVE.to(EffectFunction.ReactiveFunction(toGrave)),
        Effect.CAST.to(EffectFunction.ReactiveFunction(onCast)),
        Effect.DESTROYED.to(EffectFunction.TargetingFunction(onDestroyed)),
        Effect.DRAWN.to(EffectFunction.DependentFunction(onDraw)),
        Effect.DRAWP.to(EffectFunction.ReactiveFunction(drawPhase)),
        Effect.STANDBYP.to(EffectFunction.ReactiveFunction(standbyPhase)),
        Effect.BATTLEP.to(EffectFunction.ReactiveFunction(battlePhase)),
        Effect.ENDP.to(EffectFunction.ReactiveFunction(endPhase)),
        Effect.ATTACKING.to(EffectFunction.TargetingFunction(onAttack)),
        Effect.ATTACKED.to(EffectFunction.TargetingFunction(onAttacked)),
        Effect.DEALTDMG.to(EffectFunction.TargetingFunction(onDamageChar)),
        Effect.TOOKDMG.to(EffectFunction.TargetingFunction(onDamaged))
    ))

    companion object {
        internal enum class Tribe { ANGEL, DEMON, MAGIC, HUMAN, FAIRY, ORC }
        internal enum class CardType { MONSTER, SPELL, REACT, HERO }
        internal enum class Effect {
            GRAVE, CAST, DESTROYED, DRAWN, DRAWP, STANDBYP, BATTLEP, ENDP, ATTACKING, ATTACKED, DEALTDMG, TOOKDMG
        }
        internal sealed class EffectFunction {
            class ReactiveFunction(val effect: (Match, CardWrapper) -> Unit) : EffectFunction()
            class DependentFunction(val effect: (Boolean, Match, CardWrapper) -> Unit) : EffectFunction() {
                internal var condition: Boolean? = null
            }

            class TargetingFunction(val effect: (CardWrapper, Match, CardWrapper) -> Unit) : EffectFunction() {
                internal lateinit var condition: CardWrapper
            }
        }
    }
}