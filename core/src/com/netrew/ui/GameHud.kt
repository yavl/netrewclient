package com.netrew.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.netrew.Main
import ktx.actors.onClick
import ktx.actors.onKeyDown

class GameHud(private val main: Main) : Screen {
    private val stage = main.uiStage
    private val skin = main.skin
    val chatLabel = Label("Not connected", skin)
    val disconnectButton = TextButton("Disconnect", skin)
    val chatTextField = TextField("", skin)

    fun toggleChatTextField(show: Boolean) {
        if (chatTextField.isVisible == show && chatTextField.hasParent())
            return
        chatTextField.isVisible = show
        chatTextField.text = ""
        if (show)
            stage.addActor(chatTextField)
        else
            chatTextField.remove()
    }

    fun onChatEnter(text: String) {
        toggleChatTextField(false)
        chatLabel.setText(chatLabel.text.toString() + "\n${text}")
    }

    override fun show() {
        with(chatLabel) {
            setAlignment(Align.topLeft)
            x = 0f
            y = Gdx.graphics.height - chatLabel.height
        }
        stage.addActor(chatLabel)

        with(disconnectButton) {
            height = 30f
            x = Gdx.graphics.width - disconnectButton.width
            y = Gdx.graphics.height - disconnectButton.height
            onClick {
                main.screen = main.menu
            }
        }
        stage.addActor(disconnectButton)

        with(chatTextField) {
            width = Gdx.graphics.width.toFloat()
            onKeyDown { key ->
                if (key == Input.Keys.ENTER) {
                    onChatEnter(text)
                }
            }
        }
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
