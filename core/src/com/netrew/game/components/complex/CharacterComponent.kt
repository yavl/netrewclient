package com.netrew.game.components.complex

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Array

class CharacterComponent : Component, Pool.Poolable {
    var name = "default"
    var targetPosition = Vector2()
    var targetPositions = Array<Vector2>()
    var hasTargetPosition = false

    override fun reset() {
        hasTargetPosition = false
        targetPositions.clear()
    }
}