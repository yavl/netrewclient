package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool

class TransformComponent : Component, Pool.Poolable {
    val pos = Vector2()
    val scale = Vector2(1f, 1f)
    var rotation = 0f

    override fun reset() {
        pos.set(0f, 0f)
        scale.set(1f, 1f)
        rotation = 0f
    }
}