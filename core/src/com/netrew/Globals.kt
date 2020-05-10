package com.netrew

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.netrew.net.GameClient

object Globals {
    val assets = AssetManager()
    lateinit var client: GameClient
}