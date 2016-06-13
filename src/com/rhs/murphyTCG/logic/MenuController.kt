package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.name
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label

class MenuController() {
    fun initialize() {
        Username.text = "Hello, $name!"
    }

    //Have to store it like this so to change scene
    lateinit var main: AppMain

    @FXML private lateinit var BattleButton: Button
    @FXML fun Battle(event: ActionEvent) {
        val loader = FXMLLoader(this.javaClass.getResource("../GUI/scenes/FindBattle.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<FindBattleController>()
        controller.main = main

        main.window.scene = Scene(root)
    }

    @FXML private lateinit var DecksButton: Button
    @FXML fun Decks(event: ActionEvent) {
        throw UnsupportedOperationException("Yet to be implemented")
    }

    @FXML private lateinit var QuitButton: Button
    @FXML fun Quit(event: ActionEvent) = main.close()

    @FXML private lateinit var Username: Label
}