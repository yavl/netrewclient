package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.netrew.Globals
import com.netrew.game.World
import com.netrew.game.components.Mappers
import com.netrew.game.components.ShapeRendererComponent
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent

class BordersDrawingSystem : IteratingSystem(Family.all(SpriteComponent::class.java, TransformComponent::class.java, ShapeRendererComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = Mappers.sprite.get(entity)
        val transform = Mappers.transform.get(entity)

        val color = Color(sprite.image.color)
        val node = Globals.world.worldMap.getNodeByPosition(transform.pos, World.TILE_SIZE)
        if (Globals.world.coloredBorders[node.x][node.y] == null) {
            Globals.world.coloredBorders[node.x][node.y] = color
            Globals.world.coloredBorders[node.x][node.y]!!.a = 0f
            // todo add radius
        }
        else {
            val alpha = Globals.world.coloredBorders[node.x][node.y]!!.a
            if (alpha <= 1f) {
                Globals.world.coloredBorders[node.x][node.y]!!.a += deltaTime * 0.1f * Globals.timeScale
            }
            else {
                Globals.world.coloredBorders[node.x][node.y]!!.a = 1f
            }
        }
    }
}