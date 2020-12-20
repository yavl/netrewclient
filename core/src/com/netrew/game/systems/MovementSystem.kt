package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.netrew.game.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent

class MovementSystem : IteratingSystem(Family.all(VelocityComponent::class.java, TransformComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Mappers.transform.get(entity)
        val velocity = Mappers.velocity.get(entity)
        transform.pos.x += velocity.speed * velocity.direction.x * deltaTime
        transform.pos.y += velocity.speed * velocity.direction.y * deltaTime
    }
}