package com.rhs.murphyTCG.logic

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.network.Client
import com.rhs.murphyTCG.network.Server

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button



class FindBattleController {
    lateinit var main: AppMain

    @FXML private lateinit var HostButton: Button
    @FXML fun BecomeServer(event: ActionEvent) {
        Server
        battleInit()
    }

    @FXML private lateinit var JoinButton: Button
    @FXML fun BecomeClient(event: ActionEvent) {
        Client
        battleInit()
    }

    private fun battleInit() {
        throw NotImplementedError("Create battle pls bren")
        val loader = FXMLLoader(this.javaClass.getResource("../GUI/scenes/MainMenu.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<MenuController>()
        controller.main = main

        main.window.scene = Scene(root)
    }

    @FXML private lateinit var BackButton: Button
    @FXML fun Back(event: ActionEvent) {
        val loader = FXMLLoader(this.javaClass.getResource("../GUI/scenes/MainMenu.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<MenuController>()
        controller.main = main

        main.window.scene = Scene(root)
    }
}