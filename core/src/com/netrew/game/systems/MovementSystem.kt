package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.Globals
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent
import ktx.math.minus

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
            if (character.targetPositions.size > 0) {
                character.targetPosition = character.targetPositions[0]
                velocity.direction = (character.targetPosition - transform.pos).nor()
                character.targetPositions.removeIndex(0)
            }
            else {
                transform.pos.set(character.targetPosition)
                character.hasTargetPosition = false
                velocity.speed = 0f
                character.targetPositions.clear()
            }
        }
    }
}