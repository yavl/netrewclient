package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.netrew.game.Mappers
import com.netrew.game.components.LabelComponent

class NameLabelRenderingSystem : IteratingSystem(Family.all(LabelComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Mappers.transform.get(entity)
        val labelComponent = Mappers.label.get(entity)
        val sprite = Mappers.sprite.get(entity)

        val label = labelComponent.label
        label.x = transform.pos.x - label.width / 2f
        label.y = transform.pos.y - label.height - sprite.image.height / 2f
        // work in progress
    }
}