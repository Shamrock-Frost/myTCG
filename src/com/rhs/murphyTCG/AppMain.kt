package com.rhs.murphyTCG

import com.rhs.murphyTCG.logic.FindBattleController
import com.rhs.murphyTCG.logic.LoginController
import com.rhs.murphyTCG.logic.MenuController
import com.rhs.murphyTCG.network.Client
import com.rhs.murphyTCG.network.Server
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.net.InetAddress
import java.util.*

class AppMain : Application() {
    //The Stage needs public/internal access so it can be changed
    lateinit internal var window: Stage

    override fun start(primaryStage: Stage) {
        window = primaryStage
        window.title = "Malfeasance"

        loadLoginScene()
        //loadBattleSelect()
    }

    private fun loadLoginScene() {
        val loginLoader = FXMLLoader(this.javaClass.getResource("GUI/scenes/Login.fxml"))
        val root = loginLoader.load<Parent>()

        val controller = loginLoader.getController<LoginController>()
        controller.main = this

        window.scene = Scene(root)
        window.show()
    }

    private fun loadBattleSelect() {
        val loginLoader = FXMLLoader(this.javaClass.getResource("GUI/scenes/FindBattle.fxml"))
        val root = loginLoader.load<Parent>()

        val controller = loginLoader.getController<FindBattleController>()
        controller.main = this

        window.scene = Scene(root)
        window.show()
    }

    internal fun close() {
        Platform.runLater {
            if (isServer != null) {
                if (isServer!!) Server.server.close()
                else Client.client.close()
            }
            //Cleanup code
            window.close()
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            args.forEachIndexed { i, arg ->
                if(i == 0) IP = InetAddress.getByName(arg)
                if(i == 1) PORT = arg.toInt()
            }
            Application.launch(AppMain::class.java)
        }
    }
}