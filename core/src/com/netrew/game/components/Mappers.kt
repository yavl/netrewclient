package com.netrew.game.components

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.ObjectMap

object Mappers {
    val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val velocity = ComponentMapper.getFor(VelocityComponent::class.java)
    val sprite = ComponentMapper.getFor(SpriteComponent::class.java)
    val label = ComponentMapper.getFor(LabelComponent::class.java)
    val character = ComponentMapper.getFor(CharacterComponent::class.java)
    val shapeRenderer = ComponentMapper.getFor(ShapeRendererComponent::class.java)
}