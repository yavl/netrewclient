package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool

/**
 * @param speed pixels per second
 */
class VelocityComponent : Component, Pool.Poolable {
    var speed = 0f
    var maxSpeed = 100f
    var direction = Vector2(0f, 0f)

    override fun reset() {
        speed = 0f
        maxSpeed = 100f
        direction.set(0f, 0f)
    }
}