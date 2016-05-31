package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;

class MenuController() {
    //Have to store it like this so to change scene
    lateinit var main: AppMain

    @FXML private lateinit var BattleButton: Button
    @FXML fun Battle(event: ActionEvent) {
        println("do you want to go, punk?")
    }

    @FXML private lateinit var DecksButton: Button
    @FXML fun Decks(event: ActionEvent) {
        println("I love ${Tribe.HUMAN}s")
    }

    @FXML private lateinit var QuitButton: Button
    @FXML fun Quit(event: ActionEvent) = main.close()
}