package com.netrew.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.netrew.*
import com.netrew.game.components.*
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.complex.HouseComponent
import com.netrew.game.components.complex.TreeComponent
import com.netrew.game.pathfinding.*
import ktx.actors.onClick
import ktx.math.minus
import ktx.math.plus

/**
 * World class.
 *
 * This class represents game world, where new entities are created
 */
class World(val engine: Engine) {
    companion object {
        const val TILE_SIZE = 32f
    }
    lateinit var characterTexture: Texture
    lateinit var treeTexture: Texture
    lateinit var houseTexture: Texture
    lateinit var worldMap: FlatTiledGraph
    val gameSaver = GameSaver()
    lateinit var pathfinder: Pathfinder
    lateinit var coloredBorders: Array<Array<Color?>>
    val shapeRenderer = ShapeRenderer()
    var showBorders = true

    fun create() {
        characterTexture = Globals.assets.get<Texture>("gfx/circle.png")
        characterTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        treeTexture = Globals.assets.get<Texture>("gfx/tree.png")
        treeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        houseTexture = Globals.assets.get<Texture>("gfx/house.png")
        houseTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        createTerrain("europe")
        coloredBorders = Array(512) { row ->
            Array(512) { col ->
                null
            }
        }
    }

    fun update(deltaTime: Float) {
        if (!showBorders)
            return
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                if (coloredBorders[x][y] == null)
                    continue
                if (coloredBorders[x][y]!!.a <= 0.1f)
                    continue
                val color = coloredBorders[x][y]
                Gdx.gl.glEnable(GL20.GL_BLEND)
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.projectionMatrix = Globals.cam.combined
                shapeRenderer.setColor(color)
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                shapeRenderer.rect(worldMap[x, y].toWorldPos(TILE_SIZE).x, worldMap[x, y].toWorldPos(TILE_SIZE).y, World.TILE_SIZE, World.TILE_SIZE)
                shapeRenderer.end()
            }
        }
    }

    fun createTerrain(mapName: String) {
        val entity = engine.createEntity()

        val mapData = gameSaver.loadMap(mapName)
        val terrainTexture = mapData.terrainTexture
        val heightmapTexture = mapData.heightmapTexture
        if (!heightmapTexture.textureData.isPrepared) {
            heightmapTexture.textureData.prepare()
        }
        val heightmapPixmap = heightmapTexture.textureData.consumePixmap()
        heightmapPixmap.flipY()

        val transform = engine.createComponent(TransformComponent::class.java)
        transform.pos.set(terrainTexture.width / 2f, terrainTexture.height / 2f)
        transform.scale.set(TILE_SIZE, TILE_SIZE)
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(terrainTexture)
        with(sprite.image) {
            setScale(transform.scale.x, transform.scale.y)
            onRightClick {
                onTerrainRightClick()
            }
        }
        entity.add(sprite)
        Globals.stage.addActor(Mappers.sprite.get(entity).image)

        engine.addEntity(entity)

        worldMap = FlatTiledGraph()
        worldMap.init(heightmapPixmap)
        heightmapPixmap.dispose()
        pathfinder = Pathfinder(worldMap)

        /*
        /// create TILE TYPES labels
        run {
            for (x in 0 until FlatTiledGraph.sizeX) {
                for (y in 0 until FlatTiledGraph.sizeY) {
                    val label = Label(worldMap.getNode(x, y).type.toString(), Globals.skin)
                    label.setPosition(x * TILE_SIZE, y * TILE_SIZE)
                    if (worldMap.getNode(x, y).type == 1)
                        Globals.stage.addActor(label)
                }
            }
        }
         */

        /// spawn trees according to heightmap
        for (x in 0 until FlatTiledGraph.sizeX) {
            for (y in 0 until FlatTiledGraph.sizeY) {
                if (worldMap[x, y].type == TiledNode.TILE_TREE) {
                    createTree(x, y)
                }
            }
        }

        /// spawn trees according to population map
        val populationTexture = mapData.populationTexture
        if (!populationTexture.textureData.isPrepared) {
            populationTexture.textureData.prepare()
        }
        val populationPixmap = populationTexture.textureData.consumePixmap()
        populationPixmap.flipY()
        for (x in 0 until populationPixmap.width) {
            for (y in 0 until populationPixmap.height) {
                val color = Color(populationPixmap.getPixel(x, y))
                if (color != Color.BLACK) {
                    var pos = worldMap[x, y].toWorldPos(TILE_SIZE)
                    val offsetXY = TILE_SIZE / 2f
                    pos += offsetXY
                    createCharacter(pos, color)
                }
            }
        }
        populationPixmap.dispose()
    }

    fun createCharacter(pos: Vector2, color: Color = Color(1f, 1f, 1f, 1f), characterComponent: CharacterComponent = engine.createComponent(CharacterComponent::class.java), velocityComponent: VelocityComponent = engine.createComponent(VelocityComponent::class.java)) {
        val entity = engine.createEntity()

        val nameAssigner = NameAssigner("languages/names.txt")
        if (characterComponent.name == "default")
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
                Globals.console.log("${characterComponent.name}: ${transform.pos.x}; ${transform.pos.y}")
            }
            onHover {
                Globals.showPopupWindow(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), entity)
            }
            onHoverEnd {
                Globals.hidePopupWindow()
            }
        }
        Globals.stage.addActor(sprite.image)
        entity.add(sprite)

        val nameLabel = engine.createComponent(LabelComponent::class.java)
        with(nameLabel.label) {
            setText(characterComponent.name)
            onHover {
                Globals.showPopupWindow(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), entity)
            }
            onHoverEnd {
                Globals.hidePopupWindow()
            }
        }
        entity.add(nameLabel)

        val group = Group()
        group.isTransform = true
        group.addActor(sprite.image)
        group.addActor(nameLabel.label)
        Globals.stage.addActor(group)

        val shapeRenderer = engine.createComponent(ShapeRendererComponent::class.java)
        entity.add(shapeRenderer)

        engine.addEntity(entity)
    }

    fun createTree(x: Int, y: Int, treeComponent: TreeComponent = engine.createComponent(TreeComponent::class.java)) {
        worldMap.setNodeType(x, y, TiledNode.TILE_TREE)
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            this.pos.set(worldMap[x, y].toWorldPos(TILE_SIZE))
        }
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(treeTexture)
        with(sprite.image) {
            setColor(color)
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(Align.center)
            onClick {
                Globals.console.log("${transform.pos.x}, ${transform.pos.y}")
            }
            onHover {
                Globals.showPopupWindow(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), entity)
            }
            onHoverEnd {
                Globals.hidePopupWindow()
            }
        }
        Globals.stage.addActor(sprite.image)
        entity.add(sprite)
        entity.add(treeComponent)

        engine.addEntity(entity)
    }

    fun createHouse(x: Int, y: Int, houseComponent: HouseComponent = engine.createComponent(HouseComponent::class.java)) {
        worldMap.setNodeType(x, y, TiledNode.TILE_BUILDING)
        val entity = engine.createEntity()

        val transform = engine.createComponent(TransformComponent::class.java)
        with(transform) {
            pos.set(worldMap[x, y].toWorldPos(TILE_SIZE))
            //scale.set(0.5f, 0.5f)
        }
        entity.add(transform)

        val sprite = engine.createComponent(SpriteComponent::class.java)
        sprite.image = Image(houseTexture)
        with(sprite.image) {
            setColor(color)
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(Align.center)
            onClick {
                Globals.console.log("${transform.pos.x}, ${transform.pos.y}")
            }
        }
        Globals.stage.addActor(sprite.image)
        entity.add(sprite)
        entity.add(houseComponent)

        engine.addEntity(entity)
    }

    fun onTerrainRightClick() {
        if (Globals.clickedCharacter == null)
            return
        val transformComponent = Mappers.transform.get(Globals.clickedCharacter)
        val characterComponent = Mappers.character.get(Globals.clickedCharacter)
        val velocityComponent = Mappers.velocity.get(Globals.clickedCharacter)
        val startNode = worldMap.getNodeByPosition(Vector2(transformComponent.pos.x, transformComponent.pos.y), TILE_SIZE)
        val endNode = worldMap.getNodeByPosition(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos(), TILE_SIZE)
        characterComponent.targetPositions.clear()
        if (pathfinder.searchPath(startNode, endNode) && startNode != endNode) {
            val path = pathfinder.path
            characterComponent.hasTargetPosition = true
            characterComponent.targetPosition = path[0].toWorldPos(TILE_SIZE)
            val offsetXY = TILE_SIZE / 2f
            characterComponent.targetPosition += offsetXY
            velocityComponent.direction = (characterComponent.targetPosition - transformComponent.pos).nor()

            for (i in 1 until path.getCount()) {
                val each = path[i]
                val targetPos = each.toWorldPos(TILE_SIZE)
                targetPos.set(targetPos.x + offsetXY, targetPos.y + offsetXY)
                characterComponent.targetPositions.add(targetPos)
                Globals.console.log("${each.toWorldPos(TILE_SIZE).x}, ${each.toWorldPos(TILE_SIZE).y}")
            }
        }
    }

    fun saveGame() {
        gameSaver.save("saves/autosave.bin", engine)
    }

    fun loadGame() {
        clearEntities()
        gameSaver.load("saves/autosave.bin")
        Globals.clickedCharacter = null
    }

    /** clear in-game interactive entities */
    fun clearEntities() {
        val family = Family.one(CharacterComponent::class.java, HouseComponent::class.java, TreeComponent::class.java)

        while (engine.getEntitiesFor(family.get()).size() > 0) { // for some reason it won't remove all entities without `while`
            for (each in engine.getEntitiesFor(family.get())) {
                engine.removeEntity(each)
            }
        }
    }
}