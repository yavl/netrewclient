package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Pool

class SpriteComponent : Component, Pool.Poolable {
    val image = Image()

    override fun reset() {
        TODO("Not yet implemented")
    }

}