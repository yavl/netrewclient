package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction

class ShockWave() : Group() {
    private val fbo: FrameBuffer
    private val vertexShader: String
    private val fragmentShader: String
    private val shaderProgram: ShaderProgram
    private var time = 0f
    private var disabled = true
    private var shockWavePositionX = 0f
    private var shockWavePositionY = 0f

    init {
        vertexShader = Gdx.files.internal("shaders/wave.vert").readString()
        fragmentShader = Gdx.files.internal("shaders/wave.frag").readString()
        shaderProgram = ShaderProgram(vertexShader, fragmentShader)
        ShaderProgram.pedantic = false
        fbo = FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, true)
    }

    fun start(posX: Float, posY: Float) {
        shockWavePositionX = posX
        shockWavePositionY = posY
        val enable = RunnableAction()
        enable.runnable = Runnable { disabled = true }
        addAction(Actions.delay(1f, enable))
        disabled = false
        time = 0f
    }

    override fun act(delta: Float) {
        super.act(delta)
        time += delta
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (disabled) {
            super.draw(batch, parentAlpha)
        } else {
            batch.end()
            batch.flush()
            fbo.begin()
            batch.begin()
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
            super.draw(batch, parentAlpha)
            batch.end()
            batch.flush()
            fbo.end()
            batch.begin()
            batch.shader = shaderProgram
            var v = Vector2()
            v = Vector2(shockWavePositionX, shockWavePositionY)
            v.x = v.x / Gdx.graphics.width
            v.y = v.y / Gdx.graphics.height
            shaderProgram.setUniformf("time", time)
            shaderProgram.setUniformf("center", v)
            val texture = fbo.colorBufferTexture
            val textureRegion = TextureRegion(texture)
            // and.... FLIP!  V (vertical) only
            textureRegion.flip(false, true)
            batch.draw(textureRegion, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            batch.shader = null
        }
    }

    companion object {
        private var shockWave: ShockWave? = null
        val instance: ShockWave?
            get() {
                if (shockWave == null) {
                    shockWave = ShockWave()
                }
                return shockWave
            }
    }
}