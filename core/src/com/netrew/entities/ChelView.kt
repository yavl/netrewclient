package com.netrew.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.onClick

class ChelView(name: String, tex: Texture, stage: Stage) {
    var chelName: String
    var target: Vector2? = null
    var sprite = Image(tex)
    private val srcWidth = 32
    private val srcHeight = 32

    init {
        this.chelName = name
        sprite.setColor(1f, 1f, 1f, 1f)
        sprite.setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
        sprite.setOrigin(tex.width / 2.0f, tex.width / 2.0f)
        stage.addActor(sprite)
        sprite.onClick {
            println(chelName)
        }
    }

    fun update(batch: SpriteBatch, font: BitmapFont) {
        font.draw(batch, this.chelName, sprite.x, sprite.y - 5)
    }
}
