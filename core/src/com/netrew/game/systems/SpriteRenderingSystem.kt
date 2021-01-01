package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.game.components.Mappers
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent

class SpriteRenderingSystem : IteratingSystem(Family.all(SpriteComponent::class.java, TransformComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = Mappers.sprite.get(entity)
        val transform = Mappers.transform.get(entity)
        sprite.image.x = transform.pos.x - sprite.image.width / 2f
        sprite.image.y = transform.pos.y - sprite.image.height / 2f
        sprite.image.rotation = transform.rotation
    }
}