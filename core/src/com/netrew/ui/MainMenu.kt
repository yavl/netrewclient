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
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.complex.TreeComponent
import com.netrew.hasComponent
import com.netrew.ui.windows.CharacterPopupWindow
import com.netrew.ui.windows.TreePopupWindow
import ktx.actors.onClick
import ktx.actors.txt
import ktx.scene2d.*

class MainMenu : Screen {
    private val stage = Globals.uiStage
    lateinit var debugLabel: Label
    lateinit var scroll: ScrollPane
    val characterPopupWindow = CharacterPopupWindow()
    val treePopupWindow = TreePopupWindow()
    lateinit var timeScaleGroup: KHorizontalGroup

    init {
        Globals.mainMenu = this
        stage.addActor(characterPopupWindow)
        stage.addActor(treePopupWindow)
    }

    override fun show() {
        showDebugWindow()
        showChangeTimescaleButtons()
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
        timeScaleGroup.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
    }

    override fun dispose() {
        characterPopupWindow.dispose()
    }

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

    fun showChangeTimescaleButtons() {
        timeScaleGroup = scene2d.horizontalGroup {
            textButton("Timescale 1x").onClick {
                Globals.console.execCommand("timescale 1")
            }
            textButton("Timescale 10x").onClick {
                Globals.console.execCommand("timescale 10")
            }
            textButton("Timescale 20x").onClick {
                Globals.console.execCommand("timescale 20")
            }
        }
        timeScaleGroup.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        timeScaleGroup.align(Align.topRight)
        stage.addActor(timeScaleGroup)
    }

    fun appendDebugText(text: String) {
        debugLabel.txt = debugLabel.txt + '\n' + text
        scroll.scrollTo(0f, scroll.maxHeight, 0f, 0f)
    }

    fun showPopupWindow(mouseX: Float, mouseY: Float, entity: Entity) {
        if (entity.hasComponent(CharacterComponent::class.java)) {
            characterPopupWindow.isVisible = true
            characterPopupWindow.show()
            characterPopupWindow.update(entity)
            characterPopupWindow.setPosition(mouseX, Gdx.graphics.height.toFloat() - mouseY)
        } else if (entity.hasComponent(TreeComponent::class.java)) {
            treePopupWindow.isVisible = true
            treePopupWindow.show()
            treePopupWindow.update(entity)
            treePopupWindow.setPosition(mouseX, Gdx.graphics.height.toFloat() - mouseY)
        }
    }

    fun hidePopupWindow() {
        characterPopupWindow.hide()
        treePopupWindow.hide()
    }
}
