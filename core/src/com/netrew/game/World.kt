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
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.netrew.*
import com.netrew.game.components.*
import com.netrew.game.pathfinding.*
import ktx.actors.onClick
import ktx.math.*


class World(val mediator: Mediator, val engine: PooledEngine) {
    lateinit var characterTexture: Texture
    lateinit var treeTexture: Texture
    lateinit var tiledMap: TiledMap
    lateinit var tiledMapPixmap: Pixmap
    lateinit var tileTexture: Texture
    lateinit var worldMap: FlatTiledGraph
    val path =  TiledSmoothableGraphPath<FlatTiledNode>()
    val heuristic = TiledManhattanDistance<FlatTiledNode>()
    lateinit var pathfinder: IndexedAStarPathFinder<FlatTiledNode>
    lateinit var pathSmoother: PathSmoother<FlatTiledNode, Vector2>

    fun create() {
        characterTexture = mediator.assets().get<Texture>("circle.png")
        characterTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        treeTexture = mediator.assets().get<Texture>("tree.png")
        treeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        tiledMap = mediator.assets().get<TiledMap>("maps/europe/tilemap.tmx")
        val heightmapTexture = mediator.assets().get<Texture>("maps/europe/heightmap.png")
        if (!heightmapTexture.textureData.isPrepared) {
            heightmapTexture.textureData.prepare()
        }
        tiledMapPixmap = heightmapTexture.textureData.consumePixmap()

        createTerrain()
        createPixmapTerrain()
        createCharacter(Vector2(766f, 3993f))
        createCharacter(Vector2(786f, 4013f))

        /*
        createCharacter(Vector2(1165f, 1150f))
        createCharacter(Vector2(1046f, 1371f))
        createCharacter(Vector2(1173f, 1379f))
        createCharacter(Vector2(1134f, 1269f))

        createCharacter(Vector2(2417f, 2790f))
        createCharacter(Vector2(1871f, 1794f))
        createCharacter(Vector2(1842f, 1970f))
        createCharacter(Vector2(1715f, 1886f))
        createCharacter(Vector2(1824f, 1904f))*/
    }

    fun createTerrain() {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        transform.scale.set(1f, 1f)
        entity.add(transform)

        val tilemap = engine.createComponent(TilemapComponent::class.java)
        tilemap.tiledMap = tiledMap
        entity.add(tilemap)

        engine.addEntity(entity)

        worldMap = FlatTiledGraph()
        worldMap.init(tiledMapPixmap)

        /// create TILE TYPES labels
        /*
        run {
            for (x in 0 until FlatTiledGraph.sizeX) {
                for (y in 0 until FlatTiledGraph.sizeY) {
                    val label = Label(worldMap.getNode(x, y).type.toString(), mediator.skin())
                    label.setPosition(x * 32f * 2f, y * 32f * 2f)
                    if (worldMap.getNode(x, y).type == 1)
                        mediator.stage().addActor(label)
                }
            }
        }*/

        for (x in 0 until FlatTiledGraph.sizeX) {
            for (y in 0 until FlatTiledGraph.sizeY) {
                if (worldMap.getNode(x, y).type == 2) {
                    val pos = worldMap.getNode(x, y).toWorldPos(32, 1f)
                    createTree(pos)
                }
            }
        }

        val startNode = worldMap.getNode(0)
        val endNode = worldMap.getNode(414)
        pathfinder = IndexedAStarPathFinder<FlatTiledNode>(worldMap, true)
        pathfinder.searchNodePath(startNode, endNode, heuristic, path)
        pathSmoother = PathSmoother<FlatTiledNode, Vector2>(TiledRaycastCollisionDetector<FlatTiledNode?>(worldMap))
        val num = pathSmoother.smoothPath(path)

        if (pathfinder.metrics != null) {
            mediator.console().log("----------------- Indexed A* Path Finder Metrics -----------------")
            mediator.console().log("Visited nodes................... = " + pathfinder.metrics.visitedNodes)
            mediator.console().log("Open list additions............. = " + pathfinder.metrics.openListAdditions)
            mediator.console().log("Open list peak.................. = " + pathfinder.metrics.openListPeak)
        }
    }

    fun createPixmapTerrain() {
        val pixmap = Pixmap(200, 200, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.rgba8888(0f, 0f, 0f, 0f))
        pixmap.fill()
        pixmap.setColor(Color.BLACK)
        pixmap.drawPixel(32, 32)
        tileTexture = Texture(pixmap)

        val entity = engine.createEntity()
        val transform = engine.createComponent(TransformComponent::class.java)
        transform.pos.set(pixmap.width / 2f, pixmap.height / 2f)
        transform.scale.set(32f, 32f)
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
                onPixmapRightClick()
            }
        }
        entity.add(sprite)
        mediator.stage().addActor(Mappers.sprite.get(entity).image)
        engine.addEntity(entity)
    }

    fun createCharacter(pos: Vector2, color: Color = Color(1f, 1f, 1f, 1f)) {
        val entity = engine.createEntity()

        val nameAssigner = NameAssigner("names.txt")
        val characterComponent = engine.createComponent(CharacterComponent::class.java)
        characterComponent.name = nameAssigner.getUnassignedName()
        entity.add(characterComponent)

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(pos)
        }
        entity.add(transform)

        val velocity = engine.createComponent(VelocityComponent::class.java)
        entity.add(velocity)

        val size = 16
        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(characterTexture)
        with(sprite.image) {
            setColor(color)
            setSize(size.toFloat(), size.toFloat())
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(Align.center)
            onClick {
                Globals.clickedCharacter = entity
                mediator.console().log("${characterComponent.name}: ${transform.pos.x}; ${transform.pos.y}")
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

    fun createTree(pos: Vector2) {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(pos)
        }
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(treeTexture)
        with(sprite.image) {
            setColor(color)
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(Align.center)
        }
        mediator.stage().addActor(sprite.image)
        entity.add(sprite)

        engine.addEntity(entity)
    }

    fun onPixmapRightClick() {
        if (Globals.clickedCharacter == null)
            return
        val transformComponent = Mappers.transform.get(Globals.clickedCharacter)
        val characterComponent = Mappers.character.get(Globals.clickedCharacter)
        val velocityComponent = Mappers.velocity.get(Globals.clickedCharacter)
        val startNode = worldMap.getNodeByPosition(Vector2(transformComponent.pos.x, transformComponent.pos.y), 32, 1f)
        val endNode = worldMap.getNodeByPosition(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos(), 32, 1f)
        characterComponent.targetPositions.clear()
        path.clear()
        if (pathfinder.searchNodePath(startNode, endNode, heuristic, path) && startNode != endNode) {
            pathSmoother.smoothPath(path)
            characterComponent.hasTargetPosition = true
            characterComponent.targetPosition = path[0].toWorldPos(32, 1f)
            velocityComponent.direction = (characterComponent.targetPosition - transformComponent.pos).nor()

            for (i in 1 until path.getCount()) {
                val each = path[i]
                val offsetXY = 16f
                val targetPos = each.toWorldPos(32, 1f)
                targetPos.set(targetPos.x + offsetXY, targetPos.y + offsetXY)
                characterComponent.targetPositions.add(targetPos)
                mediator.console().log("${each.toWorldPos(32, 1f).x}, ${each.toWorldPos(32, 1f).y}")
            }
        }
    }

    fun loadMap() {

    }
}