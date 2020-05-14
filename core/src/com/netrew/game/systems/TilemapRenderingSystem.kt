package com.netrew.game.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.netrew.GameMediator
import com.netrew.game.Mappers
import com.netrew.game.components.TilemapComponent

class TilemapRenderingSystem(val mediator: GameMediator) : IteratingSystem(Family.all(TilemapComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderer = Mappers.tilemapRenderers[entity]
        renderer?.let {
            renderer.setView(mediator.camera())
            renderer.render()
        }
    }
}