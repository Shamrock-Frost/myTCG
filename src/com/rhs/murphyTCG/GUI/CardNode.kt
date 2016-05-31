package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.get
import com.rhs.murphyTCG.logic.*
import com.rhs.murphyTCG.logic.Match.Companion.Player
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
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
    val board = VBox(player1, player2)

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

//Contains a bunch of
internal class CardNode(val representing: Card, val inside: MatchNode) : StackPane(Rectangle(80.0, 131.0)) {
    val inHand = EventHandler<MouseEvent> {
        val slots: HBox = (inside.player1.center as VBox)[if(representing is Monster) 0 else 1] as HBox
        //See if the player wants to play it as a hidden card
        val hidden = CheckBox("Hide?")
        if(representing is React) {
            hidden.isVisible = false
            hidden.isSelected = true
        }
        this.children += hidden
        slots.children
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
                this.onMouseClicked = null
            }}
    }

    //TODO: Set this in battlephase init
    val monsterCombatPhase = EventHandler<MouseEvent> {
        //This should only be called on monsters, throw a runtime error and avoid later checks
        if(representing !is Monster) throw IllegalStateException("Non-monster tried to attack")
        //If they have no representings
        if(inside.representing.player2.monsters.all { it == null }) {
            val opponentBox = (inside.player2.left as VBox)[1]
            opponentBox.onMouseClicked = EventHandler {
                //Call my onAttack
                representing.onAttack(inside.representing.player2)
                //Reduce their health and if they're dead end the match
                inside.representing.player2.health -= representing.attack
                if(inside.representing.player2.health <= 0) inside.representing.endMatch()
                //remove this command
                opponentBox.onMouseClicked = null
            }
        }
        ((inside.player2.center as HBox)[0] as VBox)
            .children
            .filter { box -> (box as StackPane).children.size == 0 }
            .forEach { box -> box.onMouseClicked = EventHandler {
                //Do combat between the two and save the dead representings
                val dead = inside.representing.combat(representing, ((box as StackPane)[0] as CardNode).representing as Monster)
                val attacking = (box[0] as CardNode)
                //Remove the dead from the board and put them in the grave
                if(representing in dead) {
                    parent.childrenUnmodifiable -= this
                    (inside.player1.right as StackPane).children += this
                }
                if(attacking.representing in dead) {
                    attacking.parent.childrenUnmodifiable -= attacking
                    (inside.player2.right as StackPane).children += attacking
                }
            }}
    }
}