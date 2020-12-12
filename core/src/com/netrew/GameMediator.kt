package com.netrew

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.netrew.net.GameClient

class GameMediator {
    fun connect(ip: String, tcpPort: Int, udpPort: Int) {
         Globals.client = GameClient(ip, tcpPort, udpPort, this)
    }

    fun disconnect() {
        Globals.client.disconnect()
    }

    fun client(): GameClient {
        return Globals.client
    }

    fun assets(): AssetManager {
        return Globals.assets
    }

    fun createStage(viewport: ScreenViewport, batch: SpriteBatch) {
        Globals.stage = Stage(viewport, batch)
    }

    fun stage(): Stage {
        return Globals.stage
    }

    fun camera(): OrthographicCamera {
        return Globals.cam
    }

    fun dispose() {
        Globals.stage.dispose()
        Globals.assets.dispose()
    }

    fun skin(): Skin {
        return Globals.skin
    }

    fun addText(text: String) {
        Globals.mainMenu.appendDebugText(text)
    }
}