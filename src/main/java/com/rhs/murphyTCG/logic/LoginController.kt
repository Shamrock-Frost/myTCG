package com.rhs.murphyTCG.logic;

import com.rhs.murphyTCG.AppMain
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;

class LoginController() {
    var main: AppMain? = null

    @FXML private lateinit var LoginButton: Button

    // Event Listener on Button[#loginbutton].onAction
    @FXML
    fun Login(event: ActionEvent) = println("yaaay")
}