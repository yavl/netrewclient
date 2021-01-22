package com.netrew.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.netrew.Globals
import com.netrew.Main
import com.netrew.ui.widgets.PopupMenu
import ktx.actors.txt
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane

class MainMenu(private val main: Main) : Screen {
    private val stage = Globals.uiStage
    lateinit var debugLabel: Label
    lateinit var scroll: ScrollPane
    lateinit var popupMenu: PopupMenu

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
    }

    override fun dispose() {}

    fun showDebugWindow() {
        /// Debug menu:
        debugLabel = Label("Netrew game", Label.LabelStyle(Globals.Fonts.chatFont, Color.WHITE))
        debugLabel.setAlignment(Align.topLeft)

        scroll = scene2d.scrollPane {
            addActor(debugLabel)
        }
        val scrollWidth = Gdx.graphics.width / 4f
        val scrollHeight = Gdx.graphics.height / 4f
        scroll.debug()
        scroll.setPosition(0f, Gdx.graphics.height.toFloat() * 0.5f - scroll.height)
        scroll.setSize(scrollWidth, scrollHeight)
        scroll.style.background = null
        scroll.touchable = Touchable.disabled
        stage.addActor(scroll)
    }

    fun appendDebugText(text: String) {
        debugLabel.txt = debugLabel.txt + '\n' + text
        scroll.scrollTo(0f, scroll.maxHeight, 0f, 0f)
    }

    fun showPopupMenu(mouseX: Float, mouseY: Float, entity: Entity) {
        if (!this::popupMenu.isInitialized) {
            popupMenu = PopupMenu()
            stage.addActor(popupMenu)
        }
        else {
            popupMenu.isVisible = true
        }
        popupMenu.show()
        popupMenu.update(entity)
        popupMenu.setPosition(mouseX, Gdx.graphics.height.toFloat() - mouseY)
    }

    fun hidePopupMenu() {
        popupMenu.hide()
    }
}
