package com.netrew.game.components

import com.badlogic.ashley.core.ComponentMapper
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.complex.HouseComponent
import com.netrew.game.components.complex.TreeComponent

object Mappers {
    val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val velocity = ComponentMapper.getFor(VelocityComponent::class.java)
    val sprite = ComponentMapper.getFor(SpriteComponent::class.java)
    val label = ComponentMapper.getFor(LabelComponent::class.java)
    val character = ComponentMapper.getFor(CharacterComponent::class.java)
    val tree = ComponentMapper.getFor(TreeComponent::class.java)
    val house = ComponentMapper.getFor(HouseComponent::class.java)
    val shapeRenderer = ComponentMapper.getFor(ShapeRendererComponent::class.java)
}