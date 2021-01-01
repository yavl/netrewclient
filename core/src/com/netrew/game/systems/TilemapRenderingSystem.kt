package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.Mediator
import com.netrew.game.components.Mappers
import com.netrew.game.components.TilemapComponent

class TilemapRenderingSystem(val mediator: Mediator) : IteratingSystem(Family.all(TilemapComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderer = Mappers.tilemapRenderers[entity]
        renderer?.let {
            renderer.setView(mediator.camera())
            renderer.render()
        }
    }
}