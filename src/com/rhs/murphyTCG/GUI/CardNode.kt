package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.get
import com.rhs.murphyTCG.logic.Card
import com.rhs.murphyTCG.logic.Match
import com.rhs.murphyTCG.logic.Match.Companion.Player
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

internal class MatchNode(val representing: Match) {
    val player1 = generatePlayer(representing.player1)
    val player2 = generatePlayer(representing.player2)

    private fun generatePlayer(from: Player): BorderPane {
        val to = FXMLLoader.load<BorderPane>(javaClass.getResource("PlayerBoard.fxml"))
        //Hand to hand
        ((to.bottom as HBox)[0] as HBox).children.addAll(from.hand.map { CardNode(it, this) })
        //Deck to deck
        ((to.bottom as HBox)[1] as StackPane).children.addAll(from.deck.map { CardNode(it, this) })
        (to.center as HBox).children.addAll(Array<Node>(5, { index -> StackPane() }))
        return to
    }
}

internal class CardNode(val representing: Card, val parent: MatchNode) : Rectangle(30.0, 50.0) {
    init {
        onMouseClicked = EventHandler {
            (parent.player1.center as HBox)
                    .children
                    .filter { box -> (box as StackPane).children.size == 0 }
                    .forEach { box -> box.onMouseClicked = EventHandler {
                        (box as StackPane).children += this
                        box.parent.childrenUnmodifiable.forEach { it.onMouseClicked = null }
                    }}
        }
    }
}