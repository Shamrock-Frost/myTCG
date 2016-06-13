package com.rhs.murphyTCG.GUI

import com.rhs.murphyTCG.ServerDeck
import com.rhs.murphyTCG.get
import com.rhs.murphyTCG.isServer
import com.rhs.murphyTCG.logic.*
import com.rhs.murphyTCG.network.Server
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

internal class MatchNode(val representing: Match) {
    private val loader = FXMLLoader(this.javaClass.getResource("../GUI/scenes/BattleScene.fxml"))
    val controller = loader.getController<BattleController>()
    val scene = Scene(loader.load<VBox>())

    init {
        controller.loadFriendly(this)
        controller.loadEnemy(this)
    }
}

internal class HiddenCardNode(val hiding: CardNode) : StackPane(Rectangle(80.0, 131.0))

internal class CardNode(val representing: CardWrapper, val inside: MatchNode) : StackPane(Rectangle(80.0, 131.0)) {
    val inHand = EventHandler<MouseEvent> {
        val slots: HBox =
                if(representing.wrapping.cardType === Card.CardType.MONSTER) inside.controller.SelfMonsters
                else inside.controller.SelfCastables
        //See if the player wants to play it as a hidden card
        val hidden = CheckBox("Hide?")
        hidden.isAllowIndeterminate = false
        if(representing.wrapping.cardType === Card.CardType.REACT) {
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
    }
    //TODO: Set this in battlephase init
    val monsterCombatPhase = EventHandler<MouseEvent> {
        //If they have no monsters
        if(inside.representing.player2.monsters.all { it == null }) {
            val opponentBox = inside.controller.OppImage
            opponentBox.onMouseClicked = EventHandler {
                //Call my onAttack
                if(isServer!!) Server.server.sendToAllTCP()
                representing.wrapping.onAttack(inside.representing.player2.hero, representing.context!!)
                //Reduce their health and if they're dead end the match
                inside.representing.player2.health -= representing.wrapping.attack as Int
                if(inside.representing.player2.health <= 0) inside.representing.endMatch()
                //remove this command
                opponentBox.onMouseClicked = null
            }
        }
        inside.controller.OppMonsters
            .children
            .filter { box -> (box as StackPane).children.size == 0 }
            .forEach { box -> box.onMouseClicked = EventHandler {
                //Do combat between the two and save the dead monsters
                val dead = inside.representing.combat(representing, ((box as StackPane)[0] as CardNode).representing)
                val attacking = (box[0] as CardNode)
                //Remove the dead from the board and put them in the grave
                if(representing in dead) {
                    inside.controller.SelfMonsters.children -= this
                    inside.controller.SelfGrave.children += this
                }
                if(attacking.representing in dead) {
                    inside.controller.OppMonsters.children -= attacking
                    inside.controller.OppGrave.children += attacking
                }
            }}
        onMouseClicked = null
    }
}