package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.netrew.Globals
import com.netrew.game.components.Mappers
import com.netrew.game.components.ShapeRendererComponent
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TransformComponent

class TerritoryRenderingSystem : IteratingSystem(Family.all(SpriteComponent::class.java, TransformComponent::class.java, ShapeRendererComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = Mappers.sprite.get(entity)
        val transform = Mappers.transform.get(entity)
        val shapeRenderer = Mappers.shapeRenderer.get(entity).shapeRenderer

        val node = Globals.world.worldMap.getNodeByPosition(transform.pos, 32).toWorldPos(32)
        val color = Color(sprite.image.color)
        color.a = 0.5f
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.projectionMatrix = Globals.cam.combined
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(node.x, node.y, 32f, 32f);
        shapeRenderer.end();
    }
}