package com.rhs.murphyTCG

import tornadofx.App
import tornadofx.importStylesheet
import tornadofx.reloadStylesheetsOnFocus

class AppMain : App(){
    override val primaryView = BattleScreen::class

    init {
        importStylesheet(Styles::class)

        reloadStylesheetsOnFocus()
    }
}