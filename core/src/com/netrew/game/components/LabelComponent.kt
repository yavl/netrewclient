package com.netrew.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pool
import com.netrew.Globals

class LabelComponent() : Component, Pool.Poolable {
    var label = Label("default", Label.LabelStyle(Globals.Fonts.characterFont, Color.WHITE))

    init {
        label.setOrigin(Align.center)
        label.style.font.setUseIntegerPositions(false)
        label.setAlignment(Align.center)
    }

    override fun reset() {
        label.remove()
    }
}