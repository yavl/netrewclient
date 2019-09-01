package com.netrew

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.raw.RawChel
import com.raw.RawColor

class Chel {
    var name: String
    var target: Vector2? = null
    var sprite: Sprite = Sprite()
    private val srcX = 0
    private val srcY = 0
    private val srcWidth = 32
    private val srcHeight = 32
    lateinit var rawchel: RawChel

    constructor(name: String, tex: Texture) {
        this.name = name
        sprite!!.texture = tex
        sprite!!.setColor(1f, 1f, 1f, 1f)
        sprite!!.setRegion(srcX, srcY, srcWidth, srcHeight)
        sprite!!.setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
        sprite!!.setOrigin(tex.width / 2.0f, tex.width / 2.0f)
    }

    constructor(rawch: RawChel, tex: Texture) {
        this.rawchel = rawch
        this.name = rawch.name
        val c = rawch.color
        sprite = Sprite()
        sprite!!.setColor(c.r, c.g, c.b, c.a)

        sprite!!.texture = tex
        sprite!!.setRegion(srcX, srcY, srcWidth, srcHeight)
        sprite!!.setSize(Math.abs(srcWidth).toFloat(), Math.abs(srcHeight).toFloat())
        sprite!!.setOrigin(tex.width / 2.0f, tex.width / 2.0f)
        sprite!!.setOriginBasedPosition(rawch.position.x, rawch.position.y)
    }

    fun update(batch: SpriteBatch, font: BitmapFont) {
        sprite!!.draw(batch)
        val x = Interpolation.linear.apply(sprite!!.x, rawchel.position.x, 1f)
        val y = Interpolation.linear.apply(sprite!!.y, rawchel.position.y, 1f)
        sprite!!.setOriginBasedPosition(x, y)
        font.draw(batch, this.name, sprite!!.x, sprite!!.y - 5)
    }
}
