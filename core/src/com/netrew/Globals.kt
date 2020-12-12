package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.netrew.game.components.SpriteComponent
import com.netrew.net.GameClient
import com.netrew.ui.MainMenu

object Globals {
    val assets = AssetManager()
    lateinit var client: GameClient
    lateinit var stage: Stage
    val cam = OrthographicCamera()
    lateinit var skin: Skin

    /// Gameplay related:
    var clickedCharacter: SpriteComponent? = null

    lateinit var mainMenu: MainMenu
}

fun Vector2.toWorldPos(): Vector2 {
    val cam = Globals.cam
    val worldCoords = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
    val vec = cam.unproject(worldCoords)
    return Vector2(vec.x, vec.y)
}