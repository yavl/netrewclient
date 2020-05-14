package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Pool

class TilemapComponent : Component, Pool.Poolable {
    lateinit var tiledMap: TiledMap

    override fun reset() {
        TODO("Not yet implemented")
    }
}