package com.netrew.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.netrew.game.components.Mappers
import kotlin.math.max

class TilemapEntityListener() : EntityListener {
    override fun entityAdded(entity: Entity) {
        val unitScale = max(Mappers.transform.get(entity).scale.x, Mappers.transform.get(entity).scale.y)
        val renderer = OrthogonalTiledMapRenderer(Mappers.tilemap.get(entity).tiledMap, unitScale)
        Mappers.tilemapRenderers.put(entity, renderer)
    }

    override fun entityRemoved(entity: Entity) {
        Mappers.tilemapRenderers.get(entity).dispose()
    }
}