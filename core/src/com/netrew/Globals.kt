package com.netrew

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.widget.*
import com.netrew.game.World
import com.netrew.net.GameClient
import com.netrew.ui.MainMenu
import com.strongjoshua.console.GUIConsole

object Globals {
    const val VERSION = "0.0.1"
    const val DEFAULT_TIMESCALE = 10f
    var timeScale = 1f

    val assets = AssetManager()
    lateinit var client: GameClient
    lateinit var stage: Stage
    lateinit var uiStage: Stage
    val cam = OrthographicCamera()
    lateinit var skin: Skin
    lateinit var console: GUIConsole

    /// Gameplay related:
    var clickedCharacter: Entity? = null
    lateinit var mainMenu: MainMenu
    lateinit var bundle: I18NBundle
    lateinit var world: World

    object Fonts {
        lateinit var defaultFont: BitmapFont
        lateinit var chatFont: BitmapFont
        lateinit var characterFont: BitmapFont
    }

    fun connect(ip: String, tcpPort: Int, udpPort: Int) {
        client = GameClient(ip, tcpPort, udpPort)
    }

    fun disconnect() {
        client.disconnect()
    }

    fun client(): GameClient {
        return client
    }

    fun createConsole() {
        console = GUIConsole(skin, true, Input.Keys.GRAVE,
            VisWindow::class.java,
            VisTable::class.java, "default-pane",
            TextField::class.java,
            VisTextButton::class.java,
            VisLabel::class.java,
            VisScrollPane::class.java)
    }

    fun createStage(viewport: ScreenViewport, batch: SpriteBatch) {
        stage = Stage(viewport, batch)
    }

    fun dispose() {
        stage.dispose()
        assets.dispose()
        Fonts.defaultFont.dispose()
        Fonts.chatFont.dispose()
        console.dispose()
    }

    fun addText(text: String) {
        mainMenu.appendDebugText(text)
    }

    fun showPopupMenu(mouseX: Float, mouseY: Float, entity: Entity) {
        mainMenu.showPopupMenu(mouseX, mouseY, entity)
    }

    fun hidePopupMenu() {
        mainMenu.hidePopupMenu()
    }

    fun generateFonts() {
        Fonts.defaultFont = generateFont(24)
        Fonts.chatFont = generateFont(20)
        Fonts.characterFont = generateFont(20)
    }

    private fun generateFont(size: Int): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Ubuntu-Regular.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = size
        parameter.magFilter = Texture.TextureFilter.Linear
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        return generator.generateFont(parameter)
    }
}

/// Kotlin extensions below
fun Vector2.toWorldPos(): Vector2 {
    val cam = Globals.cam
    val worldPos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
    val vec = cam.unproject(worldPos)
    return Vector2(vec.x, vec.y)
}

inline fun <T : Actor> T.onHover(crossinline listener: T.() -> Unit): ClickListener {
    val clickListener = object : ClickListener() {
        override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) = listener()
        override fun isOver(actor: Actor?, x: Float, y: Float): Boolean {
            return super.isOver(actor, x, y)
        }
    }
    addListener(clickListener)
    return clickListener
}

inline fun <T : Actor> T.onHoverEnd(crossinline listener: T.() -> Unit): ClickListener {
    val clickListener = object : ClickListener() {
        override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) = listener()
    }
    addListener(clickListener)
    return clickListener
}

inline fun <T : Actor> T.onRightClick(crossinline listener: T.() -> Unit): ClickListener {
    val clickListener = object : ClickListener(Input.Buttons.RIGHT) {
        override fun clicked(event: InputEvent, x: Float, y: Float) = listener()
    }
    addListener(clickListener)
    return clickListener
}