package com.netrew.game

import com.badlogic.gdx.Gdx

class NameAssigner(path: String) {
    var names = mutableSetOf<String>()
    var unassignedNames = mutableSetOf<String>()
    var assignedNames = mutableSetOf<String>()

    init {
        val file = Gdx.files.internal(path)
        val contents = file.readString()
        names = contents.split('\n').toMutableSet()
    }

    fun getUnassignedName(): String {
        val randomName: String
        if (unassignedNames.size == 0)
            randomName = names.random()
        else
            randomName = unassignedNames.random()
        unassignedNames.remove(randomName)
        assignedNames.add(randomName)
        return randomName
    }

    fun retrieveName(name: String) {
        unassignedNames.add(name)
        assignedNames.remove(name)
    }
}