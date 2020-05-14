package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.netrew.game.Mappers
import com.netrew.game.components.SpriteComponent

class SpriteMovementSystem : IteratingSystem(Family.all(SpriteComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = Mappers.sprite.get(entity)
        val transform = Mappers.transform.get(entity)
        sprite.image.x = transform.pos.x
        sprite.image.y = transform.pos.y
    }
}