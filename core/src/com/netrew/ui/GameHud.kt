package com.netrew.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.netrew.Main

class GameHud(private val main: Main) : Screen {
    private val stage: Stage
    private val skin: Skin
    lateinit var characterLabel: Label
    lateinit var chatLabel: Label
    lateinit var disconnectButton: TextButton
    lateinit var chatTextField: TextField

    init {
        this.stage = main.stage
        this.skin = main.skin
    }

    override fun show() {
        characterLabel = Label("Character is not selected", skin)
        characterLabel.x = Gdx.graphics.width - characterLabel.width
        stage.addActor(characterLabel)

        chatLabel = Label("Not connected", skin)
        chatLabel.setAlignment(Align.topLeft)
        chatLabel.x = 0f
        chatLabel.y = Gdx.graphics.height - chatLabel.height
        stage.addActor(chatLabel)

        disconnectButton = TextButton("Disconnect", skin)
        disconnectButton.height = 30f
        disconnectButton.x = Gdx.graphics.width - disconnectButton.width
        disconnectButton.y = Gdx.graphics.height - disconnectButton.height
        stage.addActor(disconnectButton)
        disconnectButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                main.menu.connected = false
                main.screen = main.menu
                main.sprites.clear()
                main.menu.client.stop()
            }
        })

        chatTextField = TextField("", skin)
        chatTextField.width = Gdx.graphics.width.toFloat()
        chatTextField.isVisible = false
        stage.addActor(chatTextField)
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
        characterLabel.remove()
        chatLabel.remove()
        disconnectButton.remove()
        chatTextField.remove()
    }

    override fun dispose() {

    }

    fun update() {
        // update chatLabel on enter & receive
    }
}
