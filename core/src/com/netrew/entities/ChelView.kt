package com.netrew.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class ChelView {
    var chelName: String
    var target: Vector2? = null
    var sprite: Sprite = Sprite()
    private val srcX = 0
    private val srcY = 0
    private val srcWidth = 32
    private val srcHeight = 32

    constructor(name: String, tex: Texture) {
        this.chelName = name
        sprite.texture = tex
        sprite.setColor(1f, 1f, 1f, 1f)
        sprite.setRegion(srcX, srcY, srcWidth, srcHeight)
        sprite.setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
        sprite.setOrigin(tex.width / 2.0f, tex.width / 2.0f)
    }

    fun update(batch: SpriteBatch, font: BitmapFont) {
        sprite.draw(batch)
        font.draw(batch, this.chelName, sprite.x, sprite.y - 5)
    }
}
