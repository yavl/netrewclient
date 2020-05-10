package com.netrew.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Texture
import com.netrew.GameMediator
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent
import ktx.actors.onClick

class World(val mediator: GameMediator, val engine: PooledEngine) {
    fun create() {
        createChel(5165f, 5150f)
        createChel(5046f, 5371f)
        createChel(5173f, 5379f)
        createChel(5134f, 5269f)
        //sprites.forEach { chel -> chel.sprite.setColor(245f / 255f, 208f / 255f, 141f / 255f, 255f / 255f) }

        createChel(6417f, 6790f)
        createChel(4871f, 4794f)
        createChel(4842f, 4970f)
        createChel(4715f, 4886f)
        createChel(4824f, 4904f)
    }

    fun createChel(x: Float, y: Float) {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(x, y)
        }
        entity.add(transform)

        val size = 32
        val texture = mediator.assets().get<Texture>("circle.png")
        val sprite = engine.createComponent(SpriteComponent::class.java)
        with(sprite.image) {
            setColor(1f, 1f, 1f, 1f)
            setSize(size.toFloat(), size.toFloat())
            setOrigin(texture.width / 2.0f, texture.width / 2.0f)
            onClick {
                println("asdlokasd")
            }
        }
        entity.add(sprite)

        engine.addEntity(entity)
    }
}