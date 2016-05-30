package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.get
import com.rhs.murphyTCG.logic.*
import com.rhs.murphyTCG.logic.Match.Companion.Player
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.control.CheckBox
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

internal class MatchNode(val representing: Match) {
    val player1 = generatePlayer(representing.player1)
    val player2 = generatePlayer(representing.player2)

    private fun generatePlayer(from: Player): BorderPane {
        val to = FXMLLoader.load<BorderPane>(javaClass.getResource("PlayerBoard.fxml"))
        //Hand to hand
        ((to.bottom as HBox)[0] as HBox).children.addAll(from.hand.map { CardNode(it, this) })
        //Deck to deck
        ((to.bottom as HBox)[1] as StackPane).children.addAll(from.deck.map {
            HiddenCardNode(CardNode(it, this))
        }) //TODO: Add logic for turning up on draw
        return to
    }
}

internal class HiddenCardNode(val hiding: CardNode) : Rectangle(80.0, 131.0)

internal class CardNode(val representing: Card, val inside: MatchNode) : StackPane(Rectangle(80.0, 131.0)) {
    val inHand = EventHandler<MouseEvent> {
        val slots: HBox = (inside.player1.center as VBox)[if(representing is Monster) 0 else 1] as HBox
        //See if the player wants to play it as a hidden card
        val hidden = CheckBox("Hide?")
        this.children += hidden
        slots
                .children
                //Find an open slot
                .filter { box -> (box as StackPane).children.size == 0 }
                //When that box is clicked
                .forEach { box -> box.onMouseClicked = EventHandler {
                    //Remove the checkbox seeing if the player wants this hidden
                    this.children -= hidden
                    //Add this to the slot selected
                    (box as StackPane).children += if(hidden.isSelected) HiddenCardNode(this) else this
                    //Play this in the game representation
                    inside.representing.player1.play(this.representing, hidden.isSelected)
                    //Remove this from hand
                    this.parent.childrenUnmodifiable -= this
                    this.onMouseClicked = onBoard
                }}
    }
    val onBoard = EventHandler<MouseEvent> {
        //TODO: Add code for attacking/dying etc
    }

    init {
        onMouseClicked = inHand
    }
}