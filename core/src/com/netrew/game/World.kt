package com.netrew.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.netrew.GameMediator
import com.netrew.Globals
import com.netrew.game.components.*
import com.netrew.toWorldPos
import ktx.actors.onClick

class World(val mediator: GameMediator, val engine: PooledEngine) {
    lateinit var chelTexture: Texture
    lateinit var tiledMap: TiledMap
    lateinit var tileTexture: Texture

    fun create() {
        chelTexture = mediator.assets().get<Texture>("circle.png")
        chelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        tiledMap = mediator.assets().get<TiledMap>("tilemap/untitled.tmx")

        createTerrain()
        //createPixmapTerrain()

        createCharacter(Vector2(5165f, 5150f))
        createCharacter(Vector2(5046f, 5371f))
        createCharacter(Vector2(5173f, 5379f))
        createCharacter(Vector2(5134f, 5269f))

        createCharacter(Vector2(6417f, 6790f))
        createCharacter(Vector2(4871f, 4794f))
        createCharacter(Vector2(4842f, 4970f))
        createCharacter(Vector2(4715f, 4886f))
        createCharacter(Vector2(4824f, 4904f))
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

    fun createPixmapTerrain() {
        val pixmap = Pixmap(64, 64, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.rgba8888(0f, 39f / 100f, 65f / 100f, 0.6f))
        pixmap.fill()
        pixmap.setColor(Color.BLACK)
        pixmap.drawPixel(32, 32)
        tileTexture = Texture(pixmap)

        val entity = engine.createEntity()
        val transform = engine.createComponent(TransformComponent::class.java)
        transform.scale.set(256f, 256f)
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(tileTexture)
        with(sprite.image) {
            setScale(transform.scale.x, transform.scale.y)
            onClick {
                pixmap.setColor(Color.RED)
                pixmap.drawPixel(25, 25)
                tileTexture = Texture(pixmap)
                sprite.image = Image(tileTexture)
                // work in progress
            }
        }
        entity.add(sprite)
        mediator.stage().addActor(Mappers.sprite.get(entity).image)
        engine.addEntity(entity)
    }

    fun createCharacter(pos: Vector2, color: Color = Color(1f, 1f, 1f, 1f)) {
        val entity = engine.createEntity()

        val nameAssigner = NameAssigner("names.txt")
        val name = engine.createComponent(NameComponent::class.java)
        name.name = nameAssigner.getUnassignedName()
        entity.add(name)

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(pos)
        }
        entity.add(transform)

        val velocity = engine.createComponent(VelocityComponent::class.java)
        entity.add(velocity)

        val size = 32
        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(chelTexture)
        with(sprite.image) {
            setColor(color)
            setSize(size.toFloat(), size.toFloat())
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(Align.center)
            onClick {
                Globals.clickedCharacter = sprite
                mediator.addText("${name.name}: ${transform.pos.x}; ${transform.pos.y}")
                transform.pos.set(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos())
                println(transform.pos)
            }
        }
        entity.add(sprite)
        mediator.stage().addActor(Mappers.sprite.get(entity).image)

        val nameLabel = engine.createComponent(LabelComponent::class.java)
        nameLabel.label.setText(nameAssigner.getUnassignedName())
        entity.add(nameLabel)
        val group = Group()
        group.isTransform = true
        group.addActor(nameLabel.label)
        mediator.stage().addActor(group)

        engine.addEntity(entity)
        Mappers.entityBySpriteComponent.put(sprite, entity)
    }
}