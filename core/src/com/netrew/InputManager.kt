package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2

class InputManager(private val main: Main, private val cam: OrthographicCamera) : InputProcessor {
    private val camSpeed = 500.0f
    private val dragOld = Vector2()
    private val dragNew = Vector2()

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.RIGHT) {
            val sprite = Globals.clickedCharacter
            sprite?.moveTo(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            println(Gdx.input.x)
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        when (amountY) {
            1f -> cam.zoom += 0.15f * cam.zoom
            -1f -> cam.zoom -= 0.15f * cam.zoom
        }
        return false
    }

    fun handleInput(dt: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.translate(-camSpeed * cam.zoom * dt, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cam.translate(camSpeed * cam.zoom * dt, 0f, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.translate(0f, -camSpeed * cam.zoom * dt, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.translate(0f, camSpeed * cam.zoom * dt, 0f)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y) && main.screen === main.hud) {
            main.hud.toggleChatTextField(true)
            main.uiStage.keyboardFocus = main.hud.chatTextField
        }

        if (Gdx.input.justTouched()) {
            dragNew.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            dragOld.set(dragNew)
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            dragNew.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            if (dragNew != dragOld) {
                cam.translate((dragOld.x - dragNew.x) * cam.zoom, (dragNew.y - dragOld.y) * cam.zoom)
                dragOld.set(dragNew)
            }
        }
    }
}
