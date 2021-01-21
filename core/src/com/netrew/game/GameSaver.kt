package com.netrew.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent
import java.io.FileInputStream
import java.io.FileOutputStream

class GameSaver(val engine: Engine, val world: World) {
    val kryo = Kryo()

    init {
        kryo.register(TransformComponent::class.java)
        kryo.register(VelocityComponent::class.java)
        kryo.register(CharacterComponent::class.java)
        kryo.register(Int::class.java)
    }

    fun save(path: String) {
        val output = Output(FileOutputStream(path))

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

    fun load(path: String) {
        val input = Input(FileInputStream(path))

        try {
            val entitiesCount = kryo.readObject(input, Int::class.java)

            for (each in 0 until entitiesCount) {
                val transform = kryo.readObject(input, TransformComponent::class.java)
                val velocity = kryo.readObject(input, VelocityComponent::class.java)
                val character = kryo.readObject(input, CharacterComponent::class.java)

                world.createCharacter(transform.pos)
            }
        } catch(e: Exception) {
            println(e.message)
        }
    }
}