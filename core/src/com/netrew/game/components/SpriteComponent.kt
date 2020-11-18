package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Pool
import com.netrew.Globals

class SpriteComponent : Component, Pool.Poolable {
    lateinit var image: Image

    override fun reset() {
        TODO("Not yet implemented")
    }

    fun moveTo(x: Float, y: Float, screenPos: Boolean) {
        val x: Float
        if (screenPos) {
            x = Globals.cam.unproject(Vector3(1f, 1f, 1f))
        }
        image.addAction(Actions.moveTo(x, y, 5f))
    }
}