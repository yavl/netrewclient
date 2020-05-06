package com.netrew.ui

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.esotericsoftware.kryonet.Client
import com.netrew.GameMediator
import com.netrew.Main
import com.netrew.net.GameClient
import ktx.actors.onClick

class MainMenu(private val main: Main, val mediator: GameMediator) : Screen {
    private val stage = main.uiStage
    private val skin = main.skin
    private val nameTextField = TextField("default_name", skin)
    private val ipTextField = TextField("127.0.0.1", skin)
    private val connectButton = TextButton("Connect", skin)

    override fun show() {
        stage.addActor(ipTextField)

        nameTextField.y = ipTextField.y + ipTextField.height
        stage.addActor(nameTextField)

        connectButton.x = ipTextField.x + ipTextField.width
        connectButton.height = 30f
        connectButton.onClick {
            main.screen = main.hud
            mediator.connect(ipTextField.text, 13370, 13371)
        }
        stage.addActor(connectButton)
    }

    override fun render(delta: Float) {
        stage.draw()
        stage.act()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        nameTextField.remove()
        ipTextField.remove()
        connectButton.remove()
    }

    override fun dispose() {}
}
