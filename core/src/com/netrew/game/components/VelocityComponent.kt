package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool

class VelocityComponent : Component, Pool.Poolable {
    var speed = 0f
    val direction = Vector2(0f, 0f)

    override fun reset() {
        speed = 0f
    }
}