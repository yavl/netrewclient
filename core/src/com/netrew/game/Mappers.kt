package com.netrew.game

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.netrew.game.components.LabelComponent
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TilemapComponent
import com.netrew.game.components.TransformComponent

object Mappers {
    val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val sprite = ComponentMapper.getFor(SpriteComponent::class.java)
    val tilemap = ComponentMapper.getFor(TilemapComponent::class.java)
    val tilemapRenderers = ObjectMap<Entity, OrthogonalTiledMapRenderer>()
    val label = ComponentMapper.getFor(LabelComponent::class.java)
    val entityBySpriteComponent = ObjectMap<SpriteComponent, Entity>()
}