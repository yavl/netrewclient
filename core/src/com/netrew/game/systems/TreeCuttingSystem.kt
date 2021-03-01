package com.netrew.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.netrew.Globals
import com.netrew.game.components.complex.CharacterComponent
import com.netrew.game.components.Mappers
import com.netrew.game.components.TransformComponent
import com.netrew.game.components.VelocityComponent
import com.netrew.game.components.complex.HouseComponent
import com.netrew.game.components.complex.Resource
import com.netrew.game.components.complex.TreeComponent
import ktx.math.minus

class TreeCuttingSystem : IteratingSystem(Family.all(TransformComponent::class.java, CharacterComponent::class.java).get()) {
    // WIP
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Mappers.transform.get(entity)
        val character = Mappers.character.get(entity)

        /*
        character.targetTree = findClosestTree(entity)
        val targetTree = character.targetTree
        if (targetTree == null)
            return
        val targetTransform = Mappers.transform.get(targetTree)
        if (transform.pos.dst2(targetTransform.pos) <= 360f) {
            engine.removeEntity(targetTree)
            character.inventory[Resource.WOOD] = character.inventory[Resource.WOOD]!! + 1
        }*/
    }

    fun findClosestTree(entity: Entity): Entity? {
        val transform = Mappers.transform.get(entity)
        val treeFamily = Family.all(TransformComponent::class.java, TreeComponent::class.java)
        var closest: Entity? = null
        val closestDistance = Float.MAX_VALUE
        for (each in engine.getEntitiesFor(treeFamily.get())) {
            val treeTransform = Mappers.transform.get(each)
            if (transform.pos.dst2(treeTransform.pos) < closestDistance) {
                closest = each
            }
        }
        return closest
    }
}