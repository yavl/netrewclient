package com.netrew.ui

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.netrew.Client
import com.netrew.Main
import ktx.actors.onClick

class MainMenu(private val main: Main) : Screen {
    private val stage: Stage
    private val skin: Skin
    private var nameTextField: TextField? = null
    private var ipTextField: TextField? = null
    private var connectButton: TextButton? = null
    var connected: Boolean = false
    lateinit var client: Client
    var updatechels: Boolean = false

    init {
        this.stage = main.uiStage
        this.skin = main.skin
    }

    override fun show() {
        ipTextField = TextField("127.0.0.1", skin)
        stage.addActor(ipTextField)

        nameTextField = TextField("default_name", skin)
        nameTextField!!.y = ipTextField!!.y + ipTextField!!.height
        stage.addActor(nameTextField)

        connectButton = TextButton("Connect", skin)
        connectButton!!.x = ipTextField!!.x + ipTextField!!.width
        connectButton!!.height = 30f
        stage.addActor(connectButton)
        connectButton!!.onClick {
            val r = Runnable {
                connected = true
                client = Client(nameTextField!!.text, ipTextField!!.text, 13370, main)
                client.start()
            }
            Thread(r).start()
            main.screen = main.hud
        }
    }

    override fun render(delta: Float) {
        stage.draw()
        stage.act()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        nameTextField!!.remove()
        ipTextField!!.remove()
        connectButton!!.remove()
    }

    override fun dispose() {}
}
