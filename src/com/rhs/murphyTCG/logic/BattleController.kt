package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.GUI.CardNode
import com.rhs.murphyTCG.GUI.HiddenCardNode
import com.rhs.murphyTCG.GUI.MatchNode
import javafx.fxml.FXML
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.util.*

class BattleController {
    lateinit var main: AppMain

    internal fun initMatch(deck: Stack<CardWrapper>, hero: Card) {
        //TODO: Start a local match
    }

    internal fun loadFriendly(from: MatchNode) {
        SelfHand.children.addAll(from.representing.player1.hand.map { CardNode(it, from) })
        //Deck to deck
        SelfDeck.children.addAll(from.representing.player1.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
    }

    internal fun loadEnemy(from: MatchNode) {
        OppHand.children.addAll(from.representing.player2.hand.map { CardNode(it, from) })
        //Deck to deck
        OppDeck.children.addAll(from.representing.player2.deck.map {
            HiddenCardNode(CardNode(it, from))
        })
    }

    @FXML internal lateinit var Opponent: BorderPane

    @FXML internal lateinit var OppGrave: StackPane

    @FXML internal lateinit var OppImage: ImageView

    @FXML internal lateinit var OppBoard: VBox

    @FXML internal lateinit var OppCastables: HBox

    @FXML internal lateinit var OppMonsters: HBox

    @FXML internal lateinit var OppHand: HBox

    @FXML internal lateinit var OppDeck: StackPane

    @FXML internal lateinit var Self: BorderPane

    @FXML internal lateinit var Phases: HBox

    @FXML internal lateinit var SelfGrave: StackPane

    @FXML internal lateinit var SelfImage: ImageView

    @FXML internal lateinit var SelfHand: HBox

    @FXML internal lateinit var SelfDeck: StackPane

    @FXML internal lateinit var SelfBoard: VBox

    @FXML internal lateinit var SelfMonsters: HBox

    @FXML internal lateinit var SelfCastables: HBox
}
