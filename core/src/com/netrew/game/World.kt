package com.netrew.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.netrew.GameMediator
import com.netrew.game.components.SpriteComponent
import com.netrew.game.components.TilemapComponent
import com.netrew.game.components.TransformComponent
import ktx.actors.onClick

class World(val mediator: GameMediator, val engine: PooledEngine) {
    lateinit var chelTexture: Texture
    lateinit var tiledMap: TiledMap

    fun create() {
        chelTexture = mediator.assets().get<Texture>("circle.png")
        chelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        tiledMap = mediator.assets().get<TiledMap>("tilemap/untitled.tmx")

        createTerrain()

        createChel(Vector2(5165f, 5150f))
        createChel(Vector2(5046f, 5371f))
        createChel(Vector2(5173f, 5379f))
        createChel(Vector2(5134f, 5269f))
        //sprites.forEach { chel -> chel.sprite.setColor(245f / 255f, 208f / 255f, 141f / 255f, 255f / 255f) }

        createChel(Vector2(6417f, 6790f))
        createChel(Vector2(4871f, 4794f))
        createChel(Vector2(4842f, 4970f))
        createChel(Vector2(4715f, 4886f))
        createChel(Vector2(4824f, 4904f))
    }

    fun createTerrain() {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        transform.scale.set(4f, 4f)
        entity.add(transform)

        val tilemap = engine.createComponent(TilemapComponent::class.java)
        tilemap.tiledMap = tiledMap
        entity.add(tilemap)

        engine.addEntity(entity)
    }

    fun createChel(pos: Vector2, color: Color = Color(1f, 1f, 1f, 1f)) {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(pos)
        }
        entity.add(transform)

        val size = 32
        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(chelTexture)
        with(sprite.image) {
            setColor(1f, 1f, 1f, 1f)
            setSize(size.toFloat(), size.toFloat())
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(chelTexture.width / 2.0f, chelTexture.width / 2.0f)
            onClick {
                println("asdlokasd")
            }
        }
        entity.add(sprite)
        mediator.stage().addActor(Mappers.sprite.get(entity).image)

        engine.addEntity(entity)
    }
}