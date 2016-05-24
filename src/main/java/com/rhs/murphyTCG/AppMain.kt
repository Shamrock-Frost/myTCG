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
        try {
            val fxml = FXMLLoader(this.javaClass.getResource("/Login.fxml"))
            val root = fxml.load<Parent>()

            val controller = fxml.getController<LoginController>()
            controller.main = this

            window.scene = Scene(root)
            window.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            Application.launch(AppMain::class.java)
        }
    }
}