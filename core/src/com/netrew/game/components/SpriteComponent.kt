package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Pool

class SpriteComponent : Component, Pool.Poolable {
    lateinit var image: Image

    override fun reset() {
        image.remove()
    }
}