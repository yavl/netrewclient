package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class NameComponent : Component, Pool.Poolable {
    var name = "default"

    override fun reset() {
        TODO("Not yet implemented")
    }
}