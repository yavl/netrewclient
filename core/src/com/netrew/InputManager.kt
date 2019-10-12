package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.netrew.entities.ChelView
import com.raw.Request

class InputManager(private val main: Main, private val cam: OrthographicCamera, private val sprites: Array<ChelView>) : InputProcessor {
    private val camSpeed = 500.0f
    private val dragOld = Vector2()
    private val dragNew = Vector2()
    var selected: ChelView? = null
    var sendMessage = false

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (main.screen !== main.menu) {
            val x = Gdx.input.x
            val y = Gdx.input.y
            if (button == Input.Buttons.LEFT) {
                for (chel in sprites) {
                    val input = Vector3(x.toFloat(), y.toFloat(), 0f)
                    cam.unproject(input)
                    if (chel.sprite.boundingRectangle.contains(input.x, input.y)) {
                        selected = chel
                        main.hud.characterLabel.setText(selected!!.chelName)
                        break
                    } else {
                        selected = null
                        main.hud.characterLabel.setText("Character is not selected")
                    }
                }
            }
            if (button == Input.Buttons.RIGHT && selected != null) {
                val input = Vector3(x.toFloat(), y.toFloat(), 0f)
                cam.unproject(input)
                selected!!.target = Vector2(input.x, input.y)
                main.menu.client.requests.sendRequest(Request.SERVER_RECEIVE_TARGET)
                main.menu.client.sendLine(selected!!.chelName)
                main.menu.client.sendVector2(selected!!.target!!)
            }
        }
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

    override fun scrolled(amount: Int): Boolean {
        when (amount) {
            1 -> cam.zoom += 0.15f * cam.zoom
            -1 -> cam.zoom -= 0.15f * cam.zoom
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
            main.hud.chatTextField.isVisible = true
            main.uiStage.keyboardFocus = main.hud.chatTextField
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && main.screen === main.hud) {
            if (main.hud.chatTextField.text.length > 0) {
                sendMessage = true
            } else {
                main.hud.chatTextField.isVisible = false
            }
            main.uiStage.unfocusAll()
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
