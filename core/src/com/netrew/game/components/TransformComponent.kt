package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Pool

class TransformComponent : Component, Pool.Poolable {
    val pos = Vector2()

    override fun reset() {
        pos.set(0f, 0f)
    }
}