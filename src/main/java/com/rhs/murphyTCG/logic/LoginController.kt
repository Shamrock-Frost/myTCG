package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene

class LoginController() {
    var main: AppMain? = null

    @FXML private lateinit var LoginButton: Button

    // Event Listener on Button[#loginbutton].onAction
    @FXML
    fun Login(event: ActionEvent) {
        val loginLoader = FXMLLoader(this.javaClass.getResource("/MainMenu.fxml"))
        val root = loginLoader.load<Parent>()

        val controller = loginLoader.getController<MenuController>()
        controller.main = this.main

        main!!.window.scene = Scene(root)
    }
}