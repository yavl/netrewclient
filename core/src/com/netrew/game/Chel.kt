package com.netrew.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.netrew.game.components.TransformComponent
import ktx.actors.onClick

class Chel(val name: String, tex: Texture, stage: Stage) {
    var sprite = Image(tex)
    private val srcWidth = 32
    private val srcHeight = 32
    val entity = Entity()

    init {
        with(entity) {
            add(TransformComponent())
        }

        with(sprite) {
            setColor(1f, 1f, 1f, 1f)
            setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
            setOrigin(tex.width / 2.0f, tex.width / 2.0f)
            onClick {
                println(name)
            }
        }
        stage.addActor(sprite)
    }

    fun update(batch: SpriteBatch, font: BitmapFont) {
        font.draw(batch, name, sprite.x, sprite.y - 5)
        sprite.x = entity.getComponent(TransformComponent::class.java).pos.x
        sprite.y = entity.getComponent(TransformComponent::class.java).pos.y
    }
}
