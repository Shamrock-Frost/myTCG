package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.LoginController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException

class AppMain : Application() {
    lateinit internal var window: Stage

    override fun start(primaryStage: Stage) {
        window = primaryStage
        window.title = "Fill this eventually"

        this.initRootLayout()
    }

    /**
     * Initializes the root layout.
     */
    private fun initRootLayout() {
        val loginLoader = FXMLLoader(this.javaClass.getResource("/Login.fxml"))
        val root = loginLoader.load<Parent>()

        val controller = loginLoader.getController<LoginController>()
        controller.main = this

        window.scene = Scene(root)
        window.show()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) = Application.launch(AppMain::class.java)
    }
}