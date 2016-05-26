package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.LoginController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class AppMain : Application() {
    //The Stage needs public/internal access so it can be changed
    lateinit internal var window: Stage

    override fun start(primaryStage: Stage) {
        window = primaryStage
        window.title = "Malfeasance"

        loadLoginScene()
    }

    private fun loadLoginScene() {
        val loginLoader = FXMLLoader(this.javaClass.getResource("/Login.fxml"))
        val root = loginLoader.load<Parent>()

        val controller = loginLoader.getController<LoginController>()
        controller.main = this

        window.scene = Scene(root)
        window.show()
    }

    internal fun close() {
        //Cleanup code
        window.close()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) = Application.launch(AppMain::class.java)
    }
}