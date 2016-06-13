package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.DoNothing
import java.util.*

internal enum class Card(val cardName: String,
                         val cost: Int,
                         val tribe: Tribe,
                         val cardType: CardType,
                         val attack: Int? = null,
                         val health: Int? = null,
                         val mana: Int? = null,
                         val toGrave: (Match) -> Unit = ::DoNothing,
                         val onCast: (Match) -> Unit = ::DoNothing,
                         val onDestroyed: (Match) -> Unit = ::DoNothing,
                         val onDraw: (Boolean, Match) -> Unit = ::DoNothing,
                         val drawPhase: (Match) -> Unit = ::DoNothing,
                         val standbyPhase: (Match) -> Unit = ::DoNothing,
                         val battlePhase: (Match) -> Unit = ::DoNothing,
                         val endPhase: (Match) -> Unit = ::DoNothing,
//                         val targeted: (CardWrapper, Match) -> Unit = ::DoNothing,
                         val onAttack: (CardWrapper, Match) -> Unit = ::DoNothing,
                         val onAttacked: (CardWrapper, Match) -> Unit = ::DoNothing,
                         val onDamageChar: (CardWrapper, Match) -> Unit = ::DoNothing,
                         val onDamaged: (CardWrapper, Match) -> Unit = ::DoNothing
) {
    Test(cardName = "test", cost = 0, tribe = Tribe.HUMAN, cardType = CardType.MONSTER)
    , BadDude(cardName = "Rubicante the Rabid", cost = 0, tribe = Tribe.DEMON, cardType = CardType.HERO, health = 50, mana = 3)
    , GoodGirl(cardName = "Kushiel the Arbiter", cost = 0, tribe = Tribe.ANGEL, cardType = CardType.HERO, health = 70, mana = 0)
    ;

    //For ease of sending stuff over the network
    val effects: EnumMap<Effect, EffectFunction> = EnumMap(mapOf(
        Pair(Effect.GRAVE, EffectFunction.ReactiveFunction(toGrave)),
        Pair(Effect.CAST, EffectFunction.ReactiveFunction(onCast)),
        Pair(Effect.DESTROYED, EffectFunction.ReactiveFunction(onDestroyed)),
        Pair(Effect.DRAWN, EffectFunction.DependentFunction(onDraw)),
        Pair(Effect.DRAWP, EffectFunction.ReactiveFunction(drawPhase)),
        Pair(Effect.STANDBYP, EffectFunction.ReactiveFunction(standbyPhase)),
        Pair(Effect.BATTLEP, EffectFunction.ReactiveFunction(battlePhase)),
        Pair(Effect.ENDP, EffectFunction.ReactiveFunction(endPhase)),
        Pair(Effect.ATTACKING, EffectFunction.TargetingFunction(onAttack)),
        Pair(Effect.ATTACKED, EffectFunction.TargetingFunction(onAttacked)),
        Pair(Effect.DEALTDMG, EffectFunction.TargetingFunction(onDamageChar)),
        Pair(Effect.TOOKDMG, EffectFunction.TargetingFunction(onDamaged))
    ))

    companion object {
        internal enum class Tribe { ANGEL, DEMON, MAGIC, HUMAN, FAIRY, ORC }
        internal enum class CardType { MONSTER, SPELL, REACT, HERO }
        internal enum class Effect {
            GRAVE, CAST, DESTROYED, DRAWN, DRAWP, STANDBYP, BATTLEP, ENDP, ATTACKING, ATTACKED, DEALTDMG, TOOKDMG
        }
        internal sealed class EffectFunction {
            class ReactiveFunction(val effect: (Match) -> Unit) : EffectFunction()
            class DependentFunction(val effect: (Boolean, Match) -> Unit) : EffectFunction()
            class TargetingFunction(val effect: (CardWrapper, Match) -> Unit) : EffectFunction()
        }
    }
}