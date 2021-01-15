package com.netrew.game

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.pfa.PathSmoother
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.netrew.*
import com.netrew.game.components.*
import com.netrew.game.pathfinding.*
import ktx.actors.onClick
import ktx.math.minus
import java.io.FileInputStream
import java.io.FileOutputStream


class World(val mediator: Mediator, val engine: PooledEngine) {
    lateinit var characterTexture: Texture
    lateinit var treeTexture: Texture
    lateinit var heightmapPixmap: Pixmap
    lateinit var territoryPixmap: Pixmap
    lateinit var tileTexture: Texture
    lateinit var worldMap: FlatTiledGraph
    lateinit var terrainTexture: Texture
    val path =  TiledSmoothableGraphPath<FlatTiledNode>()
    val heuristic = TiledManhattanDistance<FlatTiledNode>()
    lateinit var pathfinder: IndexedAStarPathFinder<FlatTiledNode>
    lateinit var pathSmoother: PathSmoother<FlatTiledNode, Vector2>

    fun create() {
        characterTexture = mediator.assets().get<Texture>("circle.png")
        characterTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        treeTexture = mediator.assets().get<Texture>("tree.png")
        treeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        val heightmapTexture = mediator.assets().get<Texture>("maps/europe/heightmap.png")
        if (!heightmapTexture.textureData.isPrepared) {
            heightmapTexture.textureData.prepare()
        }
        heightmapPixmap = heightmapTexture.textureData.consumePixmap()

        terrainTexture = mediator.assets().get<Texture>("maps/europe/terrain.png")

        createTerrain()
        createTerritoryPixmap()
        createCharacter(Vector2(1360f, 10512f))
        createCharacter(Vector2(1941f, 10301f))
    }

    fun createTerrain() {
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        transform.pos.set(terrainTexture.width / 2f, terrainTexture.height / 2f)
        transform.scale.set(32f, 32f)
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(terrainTexture)
        with(sprite.image) {
            setScale(transform.scale.x, transform.scale.y)
        }
        entity.add(sprite)
        mediator.stage().addActor(Mappers.sprite.get(entity).image)

        engine.addEntity(entity)

        worldMap = FlatTiledGraph()
        worldMap.init(heightmapPixmap)

        /*
        /// create TILE TYPES labels
        run {
            for (x in 0 until FlatTiledGraph.sizeX) {
                for (y in 0 until FlatTiledGraph.sizeY) {
                    val label = Label(worldMap.getNode(x, y).type.toString(), mediator.skin())
                    label.setPosition(x * 32f, y * 32f)
                    if (worldMap.getNode(x, y).type == 1)
                        mediator.stage().addActor(label)
                }
            }
        }*/

        for (x in 0 until FlatTiledGraph.sizeX) {
            for (y in 0 until FlatTiledGraph.sizeY) {
                if (worldMap.getNode(x, y).type == 2) {
                    val pos = worldMap.getNode(x, y).toWorldPos(32)
                    createTree(pos)
                }
            }
        }

        val startNode = worldMap.getNode(0)
        val endNode = worldMap.getNode(414)
        pathfinder = IndexedAStarPathFinder<FlatTiledNode>(worldMap, true)
        pathfinder.searchNodePath(startNode, endNode, heuristic, path)
        pathSmoother = PathSmoother<FlatTiledNode, Vector2>(TiledRaycastCollisionDetector<FlatTiledNode?>(worldMap))
        pathSmoother.smoothPath(path)
    }

    fun createTerritoryPixmap() {
        territoryPixmap = Pixmap(terrainTexture.width, terrainTexture.height, Pixmap.Format.RGBA8888)
        territoryPixmap.setColor(Color.rgba8888(0f, 0f, 0f, 0f))
        territoryPixmap.fill()
        territoryPixmap.setColor(Color.BLACK)
        territoryPixmap.drawPixel(32, 32)
        tileTexture = Texture(territoryPixmap)

        val entity = engine.createEntity()
        val transform = engine.createComponent(TransformComponent::class.java)
        transform.pos.set(territoryPixmap.width / 2f, territoryPixmap.height / 2f)
        transform.scale.set(32f, 32f)
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(tileTexture)
        with(sprite.image) {
            setScale(transform.scale.x, transform.scale.y)
            onClick {
                territoryPixmap.setColor(Color.RED)
                territoryPixmap.drawPixel(33, 32)
                tileTexture = Texture(territoryPixmap)
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
        with(nameLabel.label) {
            setText(nameAssigner.getUnassignedName())
            onHover {
                mediator.showPopupMenu(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), entity)
            }
            onHoverEnd {
                mediator.hidePopupMenu()
            }
        }
        entity.add(nameLabel)
        val group = Group()
        group.isTransform = true
        group.addActor(sprite.image)
        group.addActor(nameLabel.label)
        mediator.stage().addActor(group)

        val shapeRenderer = engine.createComponent(ShapeRendererComponent::class.java)
        entity.add(shapeRenderer)

        engine.addEntity(entity)
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
        val startNode = worldMap.getNodeByPosition(Vector2(transformComponent.pos.x, transformComponent.pos.y), 32)
        val endNode = worldMap.getNodeByPosition(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos(), 32)
        characterComponent.targetPositions.clear()
        path.clear()
        if (pathfinder.searchNodePath(startNode, endNode, heuristic, path) && startNode != endNode) {
            pathSmoother.smoothPath(path)
            characterComponent.hasTargetPosition = true
            characterComponent.targetPosition = path[0].toWorldPos(32)
            val offsetXY = 16f
            characterComponent.targetPosition.x += offsetXY
            characterComponent.targetPosition.y += offsetXY
            velocityComponent.direction = (characterComponent.targetPosition - transformComponent.pos).nor()

            for (i in 1 until path.getCount()) {
                val each = path[i]
                val targetPos = each.toWorldPos(32)
                targetPos.set(targetPos.x + offsetXY, targetPos.y + offsetXY)
                characterComponent.targetPositions.add(targetPos)
                mediator.console().log("${each.toWorldPos(32).x}, ${each.toWorldPos(32).y}")
            }
        }
    }

    fun saveGame() {
        val kryo = Kryo()
        kryo.register(TransformComponent::class.java)
        kryo.register(VelocityComponent::class.java)
        kryo.register(CharacterComponent::class.java)
        kryo.register(Int::class.java)
        val output = Output(FileOutputStream("autosave.bin"))

        val family = Family.all(TransformComponent::class.java, VelocityComponent::class.java, CharacterComponent::class.java)
        val entitiesCount = engine.getEntitiesFor(family.get()).size()
        kryo.writeObject(output, entitiesCount)
        for (each in engine.getEntitiesFor(family.get())) {
            val transform = Mappers.transform.get(each)
            val velocity = Mappers.velocity.get(each)
            val character = Mappers.character.get(each)

            kryo.writeObject(output, transform)
            kryo.writeObject(output, velocity)
            kryo.writeObject(output, character)
        }
        output.close()
    }

    fun loadGame() {
        val kryo = Kryo()
        kryo.register(TransformComponent::class.java)
        kryo.register(VelocityComponent::class.java)
        kryo.register(CharacterComponent::class.java)
        kryo.register(Int::class.java)
        val input = Input(FileInputStream("autosave.bin"))

        try {
            val entitiesCount = kryo.readObject(input, Int::class.java)

            for (each in 0 until entitiesCount) {
                val transform = kryo.readObject(input, TransformComponent::class.java)
                val velocity = kryo.readObject(input, VelocityComponent::class.java)
                val character = kryo.readObject(input, CharacterComponent::class.java)

                createCharacter(transform.pos)
            }
        } catch(e: Exception) {
            println(e.message)
        }
    }

    fun clearCharacters() {
        val family = Family.all(TransformComponent::class.java, VelocityComponent::class.java, CharacterComponent::class.java)

        while (engine.getEntitiesFor(family.get()).size() > 0) { // for some reason it won't remove all entities without `while`
            for (each in engine.getEntitiesFor(family.get())) {
                engine.removeEntity(each)
            }
        }
    }
}