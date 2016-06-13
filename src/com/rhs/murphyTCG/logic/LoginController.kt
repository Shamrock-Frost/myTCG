package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import com.rhs.murphyTCG.name
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField

class LoginController {
    //Have to store it like this so to change scene
    lateinit var main: AppMain

    @FXML private lateinit var Username: TextField

    @FXML private lateinit var LoginButton: Button
    @FXML fun Login(event: ActionEvent) {
        name = Username.text

        val loader = FXMLLoader(this.javaClass.getResource("../GUI/scenes/MainMenu.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<MenuController>()
        controller.main = main

        main.window.scene = Scene(root)
    }
}