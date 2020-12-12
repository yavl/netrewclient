package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pool
import com.netrew.Globals

class TaskComponent() : Component, Pool.Poolable {
    val sequence = SequenceAction()

    init {
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}