package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.game.World
import com.netrew.game.components.Mappers
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.complex.HouseComponent

class HouseSpriteRenderingSystem : IteratingSystem(Family.all(SpriteComponent::class.java, TransformComponent::class.java, HouseComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = Mappers.sprite.get(entity)
        val transform = Mappers.transform.get(entity)
        sprite.image.x = transform.pos.x - World.TILE_SIZE / 2f
        sprite.image.y = transform.pos.y - World.TILE_SIZE / 2f
        sprite.image.rotation = transform.rotation
    }
}