package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.logic.*
import com.rhs.murphyTCG.logic.Card.Companion.CardType.*
import com.rhs.murphyTCG.network.*
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

internal class MatchNode(val representing: Match, val controller: BattleController)

internal class HiddenCardNode(val hiding: CardNode) : StackPane(Rectangle(40.0, 65.0).but {
    it.style = "-fx-fill: grey;"
    //it.isVisible = false
} )

internal class CardNode(val representing: CardWrapper, val inside: MatchNode) :
        StackPane(Rectangle(40.0, 65.0).but { it.style = "-fx-fill: white;"}, Label(representing.wrapping.cardName)) {

    init {
        this.style = "-fx-border-color: lightcyan;" +
        "-fx-border-width: 2px;"
    }

    val inHand = EventHandler<MouseEvent> {
        if(inside.representing.player1.mana < representing.wrapping.cost) return@EventHandler
        val handIndex = inside.representing.player1.hand.indexOf(this.representing)
        val isMons = representing.wrapping.cardType === MONSTER
        val slots: HBox =
                if(isMons) inside.controller.SelfMonsters
                else inside.controller.SelfCastables
        //See if the player wants to play it as a hidden card
        val hidden = CheckBox("Hide?")
        hidden.isAllowIndeterminate = false
        if(representing.wrapping.cardType === REACT) {
            hidden.isVisible = false
            hidden.isSelected = true
        }
        this.children += hidden
        slots.children
            //Find an open slot
            .filter { box -> (box as StackPane).children.size == 0 }
            //When that box is clicked
            .forEachIndexed { i, box -> box.onMouseClicked = EventHandler {
                //Remove the checkbox seeing if the player wants this hidden
                this.children -= hidden
                //Add this to the slot selected
                (box as StackPane).children += if(hidden.isSelected) HiddenCardNode(this) else this
                //Remove this from hand
                inside.controller.SelfHand.children -= this
                this.onMouseClicked = null
                slots.children.forEach { it.onMouseClicked = null }
                Platform.runLater {
                    Network.send(Summon().but {
                        it.handIndex = handIndex
                        it.boardIndex = i
                        it.hiding = hidden.isSelected
                    })

                    val nothing: (Any, Any) -> Unit = ::DoNothing //for disambiguity
                    if(representing.wrapping.onCast !== nothing) {
                        val effect = Hypothetical()
                        effect.action = Card.Companion.Effect.CAST
                        effect.activatorIndex = if(isMons) i else i + 5
                        Network.send(effect)
                    }
                }
                inside.controller.SelfMana.text = "Mana: ${inside.representing.player1.mana}"
                box.children += this
            }}
    }

    val monsterCombatPhase = EventHandler<MouseEvent> {

        onMouseClicked = null
    }
}