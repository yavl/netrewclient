package com.netrew.game

import com.badlogic.ashley.core.ComponentMapper
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TilemapComponent
import com.netrew.game.components.TransformComponent

object Mappers {
    val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val sprite = ComponentMapper.getFor(SpriteComponent::class.java)
    val tilemap = ComponentMapper.getFor(TilemapComponent::class.java)
}