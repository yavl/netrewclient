package com.netrew.game.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.netrew.game.Mappers
import com.netrew.game.components.TilemapComponent

class TilemapRenderingSystem: IteratingSystem(Family.all(TilemapComponent::class.java).get()) {
    val renderers = ObjectMap<Entity, OrthogonalTiledMapRenderer>()
    val unitScale = 4f

    override fun addedToEngine(engine: Engine) {
        for (entity in entities) {
            val renderer = OrthogonalTiledMapRenderer(Mappers.tilemap.get(entity).tiledMap, unitScale)
            renderers.put(entity, renderer)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderers[entity].render()
    }
}