package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.game.components.Mappers
import com.netrew.game.components.LabelComponent
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent

class NameLabelRenderingSystem : IteratingSystem(Family.all(
        LabelComponent::class.java,
        TransformComponent::class.java,
        SpriteComponent::class.java,
        CharacterComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Mappers.transform.get(entity)
        val labelComponent = Mappers.label.get(entity)
        val sprite = Mappers.sprite.get(entity)
        val name = Mappers.character.get(entity)

        val label = labelComponent.label
        label.x = transform.pos.x - label.width / 2f
        label.y = transform.pos.y - label.height - sprite.image.height / 2f
        label.setText(name.name)
        // work in progress
    }
}