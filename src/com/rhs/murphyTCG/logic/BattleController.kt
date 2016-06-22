package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.*
import com.rhs.murphyTCG.GUI.CardNode
import com.rhs.murphyTCG.GUI.HiddenCardNode
import com.rhs.murphyTCG.GUI.MatchNode
import com.rhs.murphyTCG.network.*
import com.rhs.murphyTCG.AppMain
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.util.Stack

class BattleController {
    lateinit var main: AppMain
    internal lateinit var match: MatchNode

    internal fun initialize(deck: Stack<Card>) {
        match = if(goingFirst!!) initMatch(deck1 = selectedDeck, hero1 = Card.GoodGirl, deck2 = deck, hero2 = Card.BadDude)
                else initMatch(deck1 = selectedDeck, hero1 = Card.BadDude, deck2 = deck, hero2 = Card.GoodGirl)
    }

    @FXML internal lateinit var OppSays: Label

    @FXML private lateinit var ChatBox: TextField
    @FXML fun Chat(e: ActionEvent) {
        val text = ChatBox.text
        ChatBox.text = ""
        Platform.runLater {
            Network.message(text)
        }
    }

    internal fun initMatch(deck1: Stack<Card>, hero1: Card, deck2: Stack<Card>, hero2: Card): MatchNode {
        val match = MatchNode(Match(deck1 = deck1, deck2 = deck2, hero1 = hero1, hero2 = hero2), this)
        loadFriendly(match)
        loadEnemy(match)
        return match
    }

    //TODO: DO these, and do on the new branch
    internal fun loadFriendly(from: MatchNode) {
        SelfHand.children.addAll(from.representing.player1.hand.map { CardNode(it, from) })
        //Deck to deck
        SelfDeck.children.addAll(from.representing.player1.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
        SelfHealth.text += from.representing.player1.health
        SelfMana.text += from.representing.player1.currMana
    }

    internal fun loadEnemy(from: MatchNode) {
        OppHand.children.addAll(from.representing.player2.hand.map { HiddenCardNode(CardNode(it, from)) })
        //Deck to deck
        OppDeck.children.addAll(from.representing.player2.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
        OppHealth.text += from.representing.player2.health
        OppMana.text += from.representing.player2.currMana
    }

    @FXML fun NextPhase(e: ActionEvent) {
        if(!match.representing.yourTurn) return
        incPhase()
        Network.send(PhaseChange())
        when(match.representing.phase) {
            0 -> {
                match.representing.player1.draw(1, true)
                Platform.runLater {
                    SelfHand.children += (SelfDeck.children.removeAt(0) as HiddenCardNode).hiding
                }
                Network.send(Draw())
            }
            1 -> {
                match.representing.player1.currMana = ++match.representing.player1.mana
                Platform.runLater {
                    SelfMana.text = "Mana: ${match.representing.player1.currMana}"
                }
                Network.send(Mana())
            }
            2, 4 -> SelfHand.children.forEach {
                val card = it as CardNode
                card.onMouseClicked = card.mainPhase
            }
            5 -> {
                match.representing.endTurn()
                Network.send(EndTurn())
                incPhase()
            }
        }
    }

    fun incPhase() {
        val index = match.representing.phase++
        match.representing.phase %= 6
        val highlight = (Phases[index] as StackPane).children.removeAt(1)
        (Phases[match.representing.phase] as StackPane).children += highlight
    }

    @FXML internal lateinit var OppHealth: Label
    @FXML internal lateinit var OppMana: Label
    @FXML internal lateinit var SelfHealth: Label
    @FXML internal lateinit var SelfMana: Label

    @FXML internal lateinit var Opponent: BorderPane

    @FXML internal lateinit var OppGrave: StackPane

    @FXML internal lateinit var OppName: Label

    @FXML internal lateinit var OppBoard: VBox

    @FXML internal lateinit var OppCastables: HBox

    @FXML internal lateinit var OppMonsters: HBox

    @FXML internal lateinit var OppHand: HBox

    @FXML internal lateinit var OppDeck: StackPane

    @FXML internal lateinit var Self: BorderPane

    @FXML internal lateinit var Phases: HBox

    @FXML internal lateinit var SelfGrave: StackPane

    @FXML internal lateinit var SelfName: Label

    @FXML internal lateinit var SelfHand: HBox

    @FXML internal lateinit var SelfDeck: StackPane

    @FXML internal lateinit var SelfBoard: VBox

    @FXML internal lateinit var SelfMonsters: HBox

    @FXML internal lateinit var SelfCastables: HBox
}
