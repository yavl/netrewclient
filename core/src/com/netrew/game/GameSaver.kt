package com.netrew.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.netrew.Globals
import com.netrew.flipY
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.complex.HouseComponent
import com.netrew.game.components.complex.TreeComponent
import com.netrew.game.pathfinding.FlatTiledGraph
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * A class to save and load game progress and settings
 */
class GameSaver() {
    val kryo = Kryo()

    init {
        kryo.register(TransformComponent::class.java)
        kryo.register(VelocityComponent::class.java)
        kryo.register(CharacterComponent::class.java)
        kryo.register(HouseComponent::class.java)
        kryo.register(TreeComponent::class.java)
        kryo.register(Int::class.java)
    }

    fun save(path: String, engine: Engine) {
        val output = Output(FileOutputStream(path))

        // write characters
        run {
            val charactersFamily = Family.all(
                TransformComponent::class.java,
                VelocityComponent::class.java,
                CharacterComponent::class.java
            )
            val charactersCount = engine.getEntitiesFor(charactersFamily.get()).size()
            kryo.writeObject(output, charactersCount)
            for (each in engine.getEntitiesFor(charactersFamily.get())) {
                val transform = Mappers.transform.get(each)
                val velocity = Mappers.velocity.get(each)
                val character = Mappers.character.get(each)

                kryo.writeObject(output, transform)
                kryo.writeObject(output, velocity)
                kryo.writeObject(output, character)
            }
        }

        // write houses
        run {
            val housesFamily = Family.all(TransformComponent::class.java, HouseComponent::class.java)
            val housesCount = engine.getEntitiesFor(housesFamily.get()).size()
            kryo.writeObject(output, housesCount)
            for (each in engine.getEntitiesFor(housesFamily.get())) {
                val transform = Mappers.transform.get(each)
                val house = Mappers.house.get(each)

                kryo.writeObject(output, transform)
                kryo.writeObject(output, house)
            }
        }

        // write trees
        run {
            val treesFamily = Family.all(TransformComponent::class.java, TreeComponent::class.java)
            val treesCount = engine.getEntitiesFor(treesFamily.get()).size()
            kryo.writeObject(output, treesCount)
            for (each in engine.getEntitiesFor(treesFamily.get())) {
                val transform = Mappers.transform.get(each)
                val tree = Mappers.tree.get(each)

                kryo.writeObject(output, transform)
                kryo.writeObject(output, tree)
            }
        }

        output.close()
    }

    fun load(path: String) {
        val input = Input(FileInputStream(path))

        try {
            // read characters
            run {
                val charactersCount = kryo.readObject(input, Int::class.java)
                for (each in 0 until charactersCount) {
                    val transform = kryo.readObject(input, TransformComponent::class.java)
                    val velocity = kryo.readObject(input, VelocityComponent::class.java)
                    val character = kryo.readObject(input, CharacterComponent::class.java)

                    Globals.world.createCharacter(
                        transform.pos,
                        characterComponent = character,
                        velocityComponent = velocity
                    )
                }
            }

            // read houses
            run {
                val housesCount = kryo.readObject(input, Int::class.java)
                for (each in 0 until housesCount) {
                    val transform = kryo.readObject(input, TransformComponent::class.java)
                    val house = kryo.readObject(input, HouseComponent::class.java)

                    val node = Globals.world.worldMap.getNodeByPosition(transform.pos, World.TILE_SIZE)
                    Globals.world.createHouse(node.x, node.y, house)
                }
            }

            // read trees
            run {
                val treesCount = kryo.readObject(input, Int::class.java)
                for (each in 0 until treesCount) {
                    val transform = kryo.readObject(input, TransformComponent::class.java)
                    val tree = kryo.readObject(input, TreeComponent::class.java)

                    val node = Globals.world.worldMap.getNodeByPosition(transform.pos, World.TILE_SIZE)
                    Globals.world.createTree(node.x, node.y, tree)
                }
            }
        } catch(e: Exception) {
            println(e.message)
        }
    }

    fun saveSettings() {
        val prefs = Gdx.app.getPreferences("NetrewPreferences")
        prefs.putFloat("cameraPosX", Globals.cam.position.x)
        prefs.putFloat("cameraPosY", Globals.cam.position.y)
        prefs.putFloat("cameraZoom", Globals.cam.zoom)
        prefs.flush()
    }

    fun loadSettings() {
        val prefs = Gdx.app.getPreferences("NetrewPreferences")
        val x = prefs.getFloat("cameraPosX", 0f)
        val y = prefs.getFloat("cameraPosY", 0f)
        val zoom = prefs.getFloat("cameraZoom", 1f)
        Globals.cam.position.set(x, y, 0f)
        Globals.cam.zoom = zoom
    }

    /** Load map assets
     * @param mapName map folder name
     * @return map name and textures */
    fun loadMap(mapName: String): MapData {
        Globals.assets.load("maps/$mapName/heightmap.png", Texture::class.java)
        Globals.assets.load("maps/$mapName/terrain.png", Texture::class.java)
        Globals.assets.load("maps/$mapName/population.png", Texture::class.java)
        Globals.assets.finishLoading()
        val terrainTexture = Globals.assets.get<Texture>("maps/$mapName/terrain.png")
        val heightmapTexture = Globals.assets.get<Texture>("maps/$mapName/heightmap.png")
        val populationTexture = Globals.assets.get<Texture>("maps/$mapName/population.png")

        return MapData(mapName, terrainTexture, heightmapTexture, populationTexture)
    }
}

/** Data class that stores map data */
data class MapData(val mapName: String, val terrainTexture: Texture, val heightmapTexture: Texture, val populationTexture: Texture)