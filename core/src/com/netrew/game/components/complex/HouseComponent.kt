package com.netrew.game.components.complex

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Array

class HouseComponent : Component, Pool.Poolable {
    var name = "default"

    override fun reset() {
    }
}