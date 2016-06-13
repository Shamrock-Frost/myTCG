package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.ClientDeck
import com.rhs.murphyTCG.DoNothing
import com.rhs.murphyTCG.get
import com.rhs.murphyTCG.isServer
import com.rhs.murphyTCG.logic.*
import com.rhs.murphyTCG.logic.Card.Companion.CardType.*
import com.rhs.murphyTCG.network.Attack
import com.rhs.murphyTCG.network.Client
import com.rhs.murphyTCG.network.Server
import com.rhs.murphyTCG.network.Summoned
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

internal class MatchNode(val representing: Match, val controller: BattleController) {

    init {
        controller.loadFriendly(this)
        controller.loadEnemy(this)
    }
}

internal class HiddenCardNode(val hiding: CardNode) : StackPane(Rectangle(80.0, 131.0))

internal class CardNode(val representing: CardWrapper, val inside: MatchNode) : StackPane(Rectangle(80.0, 131.0), Label(representing.wrapping.cardName)) {
    val inHand = EventHandler<MouseEvent> {
        val slots: HBox =
                if(representing.wrapping.cardType === MONSTER) inside.controller.SelfMonsters
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
            .forEach { box -> box.onMouseClicked = EventHandler {
                //Remove the checkbox seeing if the player wants this hidden
                this.children -= hidden
                //Add this to the slot selected
                (box as StackPane).children += if(hidden.isSelected) HiddenCardNode(this) else this
                //Play this in the game representation
                inside.representing.player1.play(this.representing, hidden.isSelected)
                //Remove this from hand
                (this.parent as StackPane).children -= this
                this.onMouseClicked = null
            }}
        if(isServer!!) Server.server.sendToAllTCP(Summoned(representing))
        else Client.client.sendTCP(Summoned(representing))
    }

    val monsterCombatPhase = EventHandler<MouseEvent> {

        onMouseClicked = null
    }
}