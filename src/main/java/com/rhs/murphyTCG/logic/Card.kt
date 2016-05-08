package com.rhs.murphyTCG.logic

interface Card {
    val cardName: String
    val cost: Int

    fun effect();
}