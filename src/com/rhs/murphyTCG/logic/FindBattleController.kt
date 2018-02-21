package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.ServerDeck
import com.rhs.murphyTCG.but
import com.rhs.murphyTCG.network.Network

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox


class FindBattleController {
    lateinit var main: AppMain

    private val loader = FXMLLoader(this.javaClass.getResource("/com/rhs/murphyTCG/GUI/scenes/BattleScene.fxml"))
    private val root = loader.load<VBox>()
    private val controller = loader.getController<BattleController>()

    @FXML private lateinit var Waiting: Label

    private fun init(server: Boolean) {
        val pane = HostButton.parent as Pane
        pane.children.forEach { it.isVisible = false }
        Waiting.isVisible = true
        val ip = Network.init(root, controller.but { it.main = main }, server)
        Waiting.text += "\nIP is: $ip"
    }

    @FXML private lateinit var HostButton: Button
    @FXML fun BecomeServer(event: ActionEvent) = init(true)

    @FXML private lateinit var JoinButton: Button
    @FXML fun BecomeClient(event: ActionEvent) = init(false)

    @FXML private lateinit var BackButton: Button
    @FXML fun Back(event: ActionEvent) {
        val loader = FXMLLoader(this.javaClass.getResource("/com/rhs/murphyTCG/GUI/scenes/MainMenu.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<MenuController>()
        controller.main = main

        main.window.scene = Scene(root)
    }
}