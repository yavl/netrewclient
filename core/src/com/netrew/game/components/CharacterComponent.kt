package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool

class CharacterComponent : Component, Pool.Poolable {
    var name = "default"
    var targetPosition = Vector2(0f, 0f)
    var hasTargetPosition = false

    override fun reset() {
        targetPosition.set(0f, 0f)
        hasTargetPosition = false
    }
}