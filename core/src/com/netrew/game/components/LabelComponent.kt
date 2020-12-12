package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pool
import com.netrew.Globals

class LabelComponent() : Component, Pool.Poolable {
    var text = "default"
    val label = Label(text, Globals.skin)

    init {
        label.setOrigin(Align.center)
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}