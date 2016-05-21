package com.rhs.murphyTCG

import com.rhs.murphyTCG.GUI.MainMenu
import javafx.scene.Scene
import javafx.stage.Stage
import tornadofx.App
import tornadofx.find
import tornadofx.importStylesheet

class AppMain : App() {
    override val primaryView = MainMenu::class
    lateinit var stage: Stage

    override fun start(defaultStage: Stage) {
        importStylesheet(Styles::class)
        super.start(stage)
        stage = defaultStage
    }
}