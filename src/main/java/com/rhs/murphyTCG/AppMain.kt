package com.rhs.murphyTCG

import com.rhs.murphyTCG.GUI.BattleScreen
import com.rhs.murphyTCG.GUI.Styles
import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class AppMain : App(){
    override val primaryView = BattleScreen::class

    override fun start(stage: Stage) {
        importStylesheet(Styles::class)
        super.start(stage)

    }
}