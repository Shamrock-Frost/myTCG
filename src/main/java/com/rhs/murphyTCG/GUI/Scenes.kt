package com.rhs.murphyTCG.GUI

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

fun getLoginScreen(vararg handlers: EventHandler<ActionEvent>): Scene {
    val username = TextField("Username")
    val password = TextField("Password")
    val button1 = Button("Login")
    button1.onAction = handlers[0]

    /* val grid = GridPane()
    grid.padding = Insets(10.0, 10.0, 10.0, 10.0)
    grid.vgap = 8.0
    grid.hgap = 10.0 */

    //Layout 1 - Children are vertical
    val layout = VBox(20.0, username, password, button1)
    layout.alignment = Pos.CENTER

    return Scene(layout, 0.9 * WIDTH, 0.9 * HEIGHT)
}

fun getMainMenu(vararg handlers: EventHandler<ActionEvent>): Scene {
    val label2 = Label("Scene 2")

    //Layout 1 - Children are vertical
    val layout2 = StackPane(label2)

    return Scene(layout2, 0.9 * WIDTH, 0.9 * HEIGHT)
}