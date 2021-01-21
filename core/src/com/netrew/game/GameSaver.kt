package com.netrew.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.complex.HouseComponent
import java.io.FileInputStream
import java.io.FileOutputStream

class GameSaver(val engine: Engine, val world: World) {
    val kryo = Kryo()

    init {
        kryo.register(TransformComponent::class.java)
        kryo.register(VelocityComponent::class.java)
        kryo.register(CharacterComponent::class.java)
        kryo.register(HouseComponent::class.java)
        kryo.register(Int::class.java)
    }

    fun save(path: String) {
        val output = Output(FileOutputStream(path))

        // write characters
        val charactersFamily = Family.all(TransformComponent::class.java, VelocityComponent::class.java, CharacterComponent::class.java)
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

        // write houses
        val housesFamily = Family.all(TransformComponent::class.java, HouseComponent::class.java)
        val housesCount = engine.getEntitiesFor(housesFamily.get()).size()
        kryo.writeObject(output, housesCount)
        for (each in engine.getEntitiesFor(housesFamily.get())) {
            val transform = Mappers.transform.get(each)
            val house = Mappers.house.get(each)

            kryo.writeObject(output, transform)
            kryo.writeObject(output, house)
        }

        output.close()
    }

    fun load(path: String) {
        val input = Input(FileInputStream(path))

        try {
            // read characters
            val charactersCount = kryo.readObject(input, Int::class.java)
            for (each in 0 until charactersCount) {
                val transform = kryo.readObject(input, TransformComponent::class.java)
                val velocity = kryo.readObject(input, VelocityComponent::class.java)
                val character = kryo.readObject(input, CharacterComponent::class.java)

                world.createCharacter(transform.pos)
            }

            // read houses
            val housesCount = kryo.readObject(input, Int::class.java)
            for (each in 0 until housesCount) {
                val transform = kryo.readObject(input, TransformComponent::class.java)
                val house = kryo.readObject(input, HouseComponent::class.java)

                val node = world.worldMap.getNodeByPosition(transform.pos, World.TILE_SIZE)
                world.createHouse(node.x, node.y)
            }
        } catch(e: Exception) {
            println(e.message)
        }
    }
}