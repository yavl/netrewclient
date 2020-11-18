package com.netrew

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.netrew.game.components.SpriteComponent
import com.netrew.net.GameClient
import com.netrew.ui.GameHud

object Globals {
    val assets = AssetManager()
    lateinit var client: GameClient
    lateinit var stage: Stage
    val cam = OrthographicCamera()
    var clickedCharacter: SpriteComponent? = null
}