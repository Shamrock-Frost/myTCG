package com.rhs.murphyTCG

import com.rhs.murphyTCG.GUI.getLoginScreen
import com.rhs.murphyTCG.GUI.getMainMenu
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage

class AppMain : Application() {
    lateinit var window: Stage
    var LoginScreen: Scene = getLoginScreen(EventHandler { window.scene = MainMenu })
    var MainMenu: Scene = getMainMenu()

    override fun start(primaryStage: Stage) {
        window = primaryStage
        window.title = "Fill this eventually"

        window.scene = LoginScreen
        window.show()
    }
}