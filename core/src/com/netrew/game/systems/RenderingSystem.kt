package com.netrew.game.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.scenes.scene2d.Stage
import com.netrew.game.Mappers
import com.netrew.game.components.SpriteComponent

class RenderingSystem(val stage: Stage, priority: Int) : EntitySystem(priority) {
    lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent::class.java).get())

        // VERY VERY BAD AND TEMPORARY
        for (entity in entities) {
            stage.addActor(Mappers.sprite.get(entity).image)
        }
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val sprite = Mappers.sprite.get(entity)
            val transform = Mappers.transform.get(entity)
            sprite.image.x = transform.pos.x
            sprite.image.y = transform.pos.y
        }
        stage.act()
        stage.draw()
    }
}