package com.netrew.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.netrew.GameMediator
import com.netrew.Globals
import com.netrew.Main
import ktx.actors.txt
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane

class MainMenu(private val main: Main, val mediator: GameMediator) : Screen {
    private val stage = main.uiStage
    private val skin = mediator.skin()
    private val nameTextField = TextField("default_name", skin)
    private val ipTextField = TextField("127.0.0.1", skin)
    private val connectButton = TextButton("Connect", skin)
    lateinit var debugLabel: Label
    lateinit var scroll: ScrollPane

    init {
        Globals.mainMenu = this
    }

    override fun show() {
        showDebugWindow()
    }

    override fun render(delta: Float) {
        stage.draw()
        stage.act()
    }

    override fun resize(width: Int, height: Int) {
        val scrollWidth = Gdx.graphics.width / 3f
        val scrollHeight = Gdx.graphics.height / 4f
        scroll.setPosition(0f, Gdx.graphics.height.toFloat() * 0.5f - scroll.height)
        scroll.setSize(scrollWidth, scrollHeight)
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

    fun showDebugWindow() {
        /// Debug menu:
        debugLabel = scene2d.label("Netrew gameфыв")
        debugLabel.setAlignment(Align.topLeft)
        debugLabel.setFontScale(1.5f)

        scroll = scene2d.scrollPane {
            addActor(debugLabel)
        }
        val scrollWidth = Gdx.graphics.width / 4f
        val scrollHeight = Gdx.graphics.height / 4f
        scroll.debug()
        scroll.setPosition(0f, Gdx.graphics.height.toFloat() * 0.5f - scroll.height)
        scroll.setSize(scrollWidth, scrollHeight)
        scroll.style.background = null
        stage.addActor(scroll)
    }

    fun appendDebugText(text: String) {
        debugLabel.txt = debugLabel.txt + '\n' + text
        scroll.scrollTo(0f, scroll.maxHeight, 0f, 0f)
    }
}
