package com.netrew.entities

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import ktx.actors.*

class ChelView {
    var chelName: String
    var target: Vector2? = null
    lateinit var sprite: Image
    private val srcWidth = 32
    private val srcHeight = 32

    constructor(name: String, tex: Texture, stage: Stage) {
        this.chelName = name
        sprite = Image(tex)
        sprite.setColor(1f, 1f, 1f, 1f)
        sprite.setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
        sprite.setOrigin(tex.width / 2.0f, tex.width / 2.0f)
        stage.addActor(sprite)
        sprite.onTouchDown {
            println("asdasd")
        }
        sprite.onClick {
            println(chelName)
        }
    }

    fun update(batch: SpriteBatch, font: BitmapFont) {
        sprite.draw(batch, 1f)
        font.draw(batch, this.chelName, sprite.x, sprite.y - 5)
    }
}
