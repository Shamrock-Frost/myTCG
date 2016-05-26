package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene

class MenuController() {
    lateinit var main: AppMain

    @FXML private lateinit var BattleButton: Button
    @FXML
    fun Battle(event: ActionEvent) {
        println("do you want to go, punk?")
    }

    @FXML private lateinit var DecksButton: Button
    @FXML
    fun Decks(event: ActionEvent) {
        println("most guys have decks.")
    }

    @FXML private lateinit var QuitButton: Button
    @FXML
    fun Quit(event: ActionEvent) = main!!.close()
}