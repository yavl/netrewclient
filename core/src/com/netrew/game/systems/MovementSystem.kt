package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.Globals
import com.netrew.game.components.CharacterComponent
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent

class MovementSystem : IteratingSystem(Family.all(TransformComponent::class.java, VelocityComponent::class.java, CharacterComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Mappers.transform.get(entity)
        val velocity = Mappers.velocity.get(entity)
        val character = Mappers.character.get(entity)
        transform.pos.x += velocity.speed * velocity.direction.x
        transform.pos.y += velocity.speed * velocity.direction.y

        if (character.hasTargetPosition) {
            velocity.speed = velocity.maxSpeed * Globals.timeScale * deltaTime
        }
        // stop character when target is reached
        if (character.hasTargetPosition && character.targetPosition.dst(transform.pos) <= velocity.speed) {
            character.hasTargetPosition = false
            velocity.speed = 0f
        }
    }
}