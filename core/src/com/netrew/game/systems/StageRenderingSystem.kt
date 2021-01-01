package com.netrew.game.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.scenes.scene2d.Stage

class StageRenderingSystem(val stage: Stage, priority: Int) : EntitySystem() {
    override fun update(deltaTime: Float) {
        stage.act()
        stage.draw()
    }
}