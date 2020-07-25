package com.netrew

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.netrew.net.GameClient
import com.netrew.ui.GameHud

object Globals {
    val assets = AssetManager()
    lateinit var client: GameClient
    lateinit var stage: Stage
    val hud = GameHud()
    val cam = OrthographicCamera()
}