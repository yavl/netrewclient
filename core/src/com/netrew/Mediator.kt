package com.netrew

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.netrew.game.World
import com.netrew.net.GameClient
import com.strongjoshua.console.GUIConsole

class Mediator {
    fun connect(ip: String, tcpPort: Int, udpPort: Int) {
         Globals.client = GameClient(ip, tcpPort, udpPort, this)
    }

    fun disconnect() {
        Globals.client.disconnect()
    }

    fun client(): GameClient {
        return Globals.client
    }

    fun createConsole() {
        Globals.console = GUIConsole(Globals.skin, true, Input.Keys.GRAVE)
    }

    fun console(): GUIConsole {
        return Globals.console
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

    fun uiStage(): Stage {
        return Globals.uiStage
    }

    fun camera(): OrthographicCamera {
        return Globals.cam
    }

    fun dispose() {
        Globals.stage.dispose()
        Globals.assets.dispose()
        Globals.defaultFont.dispose()
        Globals.chatFont.dispose()
        Globals.console.dispose()
    }

    fun skin(): Skin {
        return Globals.skin
    }

    fun addText(text: String) {
        Globals.mainMenu.appendDebugText(text)
    }

    fun showPopupMenu(mouseX: Float, mouseY: Float, entity: Entity) {
        Globals.mainMenu.showPopupMenu(mouseX, mouseY, entity)
    }

    fun hidePopupMenu() {
        Globals.mainMenu.hidePopupMenu()
    }

    fun bundle(): I18NBundle {
        return Globals.bundle
    }

    fun world(): World {
        return Globals.world
    }

    fun version(): String {
        return Globals.version
    }
}