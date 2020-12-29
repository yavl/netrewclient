package com.netrew.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.pfa.PathSmoother
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.netrew.*
import com.netrew.game.components.*
import com.netrew.game.pathfinding.*
import ktx.actors.onClick


class World(val mediator: Mediator, val engine: PooledEngine) {
    lateinit var chelTexture: Texture
    lateinit var tiledMap: TiledMap
    lateinit var tiledMapPixmap: Pixmap
    lateinit var tileTexture: Texture
    lateinit var worldMap: FlatTiledGraph

    fun create() {
        chelTexture = mediator.assets().get<Texture>("circle.png")
        chelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        tiledMap = mediator.assets().get<TiledMap>("tilemap/untitled.tmx")
        val heightmapTexture = mediator.assets().get<Texture>("tilemap/heightmap.png")
        if (!heightmapTexture.textureData.isPrepared) {
            heightmapTexture.textureData.prepare()
        }
        tiledMapPixmap = heightmapTexture.textureData.consumePixmap()

        createTerrain()
        createPixmapTerrain()

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

        worldMap = FlatTiledGraph()
        worldMap.init(tiledMapPixmap)

        val path =  TiledSmoothableGraphPath<FlatTiledNode>()
        val heuristic = TiledManhattanDistance<FlatTiledNode>()
        val pathfinder = IndexedAStarPathFinder<FlatTiledNode>(worldMap, true)

        val startNode = worldMap.getNode(0)
        val endNode = worldMap.getNode(414)
        pathfinder.searchNodePath(startNode, endNode, heuristic, path)
        val pathSmoother = PathSmoother<FlatTiledNode, Vector2>(TiledRaycastCollisionDetector<FlatTiledNode?>(worldMap))
        val num = pathSmoother.smoothPath(path)

        if (pathfinder.metrics != null) {
            mediator.console().log("----------------- Indexed A* Path Finder Metrics -----------------")
            mediator.console().log("Visited nodes................... = " + pathfinder.metrics.visitedNodes)
            mediator.console().log("Open list additions............. = " + pathfinder.metrics.openListAdditions)
            mediator.console().log("Open list peak.................. = " + pathfinder.metrics.openListPeak)
        }
    }

    fun createPixmapTerrain() {
        val pixmap = Pixmap(100, 100, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.rgba8888(0f, 0f, 0f, 0f))
        pixmap.fill()
        pixmap.setColor(Color.BLACK)
        pixmap.drawPixel(32, 32)
        tileTexture = Texture(pixmap)

        val entity = engine.createEntity()
        val transform = engine.createComponent(TransformComponent::class.java)
        transform.pos.set(pixmap.width / 2f, pixmap.height / 2f)
        transform.scale.set(128f, 128f)
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(tileTexture)
        with(sprite.image) {
            setScale(transform.scale.x, transform.scale.y)
            onClick {
                pixmap.setColor(Color.RED)
                pixmap.drawPixel(33, 32)
                tileTexture = Texture(pixmap)
                sprite.image.drawable = TextureRegionDrawable(tileTexture)
            }
            onRightClick {
                TODO()
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
                mediator.console().log("${name.name}: ${transform.pos.x}; ${transform.pos.y}")
                transform.pos.set(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos())
            }
            onHover {
                mediator.showPopupMenu(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), entity)
            }
            onHoverEnd {
                mediator.hidePopupMenu()
            }
        }
        mediator.stage().addActor(sprite.image)
        entity.add(sprite)

        val nameLabel = engine.createComponent(LabelComponent::class.java)
        nameLabel.label.setText(nameAssigner.getUnassignedName())
        entity.add(nameLabel)
        val group = Group()
        group.isTransform = true
        group.addActor(sprite.image)
        group.addActor(nameLabel.label)
        mediator.stage().addActor(group)

        engine.addEntity(entity)
        Mappers.entityBySpriteComponent.put(sprite, entity)
    }

    private fun asd() {

    }
}