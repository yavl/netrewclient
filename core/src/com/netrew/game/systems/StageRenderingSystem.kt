package com.netrew.game.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.scenes.scene2d.Stage
import com.netrew.game.Mappers
import com.netrew.game.components.SpriteComponent

class StageRenderingSystem(val stage: Stage, priority: Int) : EntitySystem() {
    override fun update(deltaTime: Float) {
        stage.act()
        stage.draw()
    }
}