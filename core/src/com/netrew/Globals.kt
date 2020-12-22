package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Null
import com.netrew.game.World
import com.netrew.game.components.SpriteComponent
import com.netrew.net.GameClient
import com.netrew.ui.MainMenu
import com.strongjoshua.console.GUIConsole

object Globals {
    val assets = AssetManager()
    lateinit var client: GameClient
    lateinit var stage: Stage
    lateinit var uiStage: Stage
    val cam = OrthographicCamera()
    lateinit var skin: Skin
    lateinit var console: GUIConsole

    /// Gameplay related:
    var clickedCharacter: SpriteComponent? = null
    lateinit var mainMenu: MainMenu
    lateinit var bundle: I18NBundle
    lateinit var world: World

    // Fonts
    lateinit var defaultFont: BitmapFont
    lateinit var chatFont: BitmapFont
}

/// Kotlin extensions below
fun Vector2.toWorldPos(): Vector2 {
    val cam = Globals.cam
    val worldCoords = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
    val vec = cam.unproject(worldCoords)
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