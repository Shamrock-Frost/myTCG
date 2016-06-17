package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.GUI.CardNode
import com.rhs.murphyTCG.GUI.HiddenCardNode
import com.rhs.murphyTCG.GUI.MatchNode
import com.rhs.murphyTCG.ServerDeck
import com.rhs.murphyTCG.but
import com.rhs.murphyTCG.isServer
import com.rhs.murphyTCG.network.Client
import com.rhs.murphyTCG.network.Chat
import com.rhs.murphyTCG.network.Server
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

    internal fun initialize() {
        initMatch(deck1 = ServerDeck, hero1 = Card.GoodGirl, deck2 = ServerDeck, hero2 = Card.BadDude)
        println("Heyyyy")
    }

    @FXML internal lateinit var OppSays: Label

    @FXML private lateinit var ChatBox: TextField
    @FXML fun Chat(e: ActionEvent) {
        val text = ChatBox.text
        ChatBox.text = ""
        Platform.runLater {
            if (isServer!!) Server.send(message = text)
            else Client.send(message = text)
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
        SelfHand.children.map { it.onMouseClicked = (it as CardNode).inHand }
        //Deck to deck
        SelfDeck.children.addAll(from.representing.player1.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
        SelfHealth.text += from.representing.player1.health
        SelfMana.text += from.representing.player1.mana

    }

    internal fun loadEnemy(from: MatchNode) {
        OppHand.children.addAll(from.representing.player2.hand.map { HiddenCardNode(CardNode(it, from)) })
        //Deck to deck
        OppDeck.children.addAll(from.representing.player2.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
        OppHealth.text += from.representing.player2.health
        OppMana.text += from.representing.player2.mana
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
