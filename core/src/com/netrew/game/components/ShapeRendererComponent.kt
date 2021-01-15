package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Pool

class ShapeRendererComponent : Component, Pool.Poolable {
    val shapeRenderer = ShapeRenderer()

    override fun reset() {
    }
}