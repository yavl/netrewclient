package com.netrew.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.math.Vector2
import com.netrew.Globals
import com.netrew.toWorldPos
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ConsoleCommandExecutor() : CommandExecutor() {
    @ConsoleDoc(description = "Shows time in system time zone.")
    fun time() {
        console.log("Current time: " + DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.now()).toString())
    }

    @ConsoleDoc(description = "Show game timescale.")
    fun timescale() {
        console.log("Timescale is set to ${Globals.timeScale}x")
    }

    @ConsoleDoc(description = "Set game timescale.")
    fun timescale(scale: Float) {
        Globals.timeScale = scale
        console.log("Timescale is set to ${scale}x")
    }

    @ConsoleDoc(description = "Set camera position (x, y).")
    fun cam(x: Float, y: Float) {
        Globals.cam.position.set(x, y, 0f)
    }

    @ConsoleDoc(description = "Set camera zoom.")
    fun zoom(zoom: Float) {
        Globals.cam.zoom = zoom
    }

    @ConsoleDoc(description = "Show debug info.")
    fun debug(enabled: Boolean) {
        Globals.stage.isDebugAll = enabled
        Globals.uiStage.isDebugAll = enabled
    }

    @ConsoleDoc(description = "Show version info.")
    fun version() {
        console.log(Globals.VERSION)
    }

    @ConsoleDoc(description = "Show cursor world pos (x, y).")
    fun cursor() {
        console.log("${Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos()}")
    }

    @ConsoleDoc(description = "Spawn character at cursor pos.")
    fun spawn() {
        Globals.world.createCharacter(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos())
    }

    @ConsoleDoc(description = "Spawn tree at cursor pos.")
    fun tree() {
        val node = Globals.world.worldMap.getNodeByPosition(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos(), World.TILE_SIZE)
        Globals.world.createTree(node.x, node.y)
    }

    @ConsoleDoc(description = "Spawn house at cursor pos.")
    fun house() {
        val node = Globals.world.worldMap.getNodeByPosition(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos(), World.TILE_SIZE)
        Globals.world.createHouse(node.x, node.y)
    }

    @ConsoleDoc(description = "Save game.")
    fun save() {
        Globals.world.saveGame()
    }

    @ConsoleDoc(description = "Load game.")
    fun load() {
        Globals.world.loadGame()
    }

    @ConsoleDoc(description = "Remove all characters.")
    fun clear() {
        Globals.world.clearEntities()
    }

    @ConsoleDoc(description = "Toggle borders layer visibility.")
    fun borders() {
        Globals.world.showBorders = !Globals.world.showBorders
        console.log("showBorders is set to ${Globals.world.showBorders}")
    }

    @ConsoleDoc(description = "Execute commands from file.")
    fun exec(path: String) {
        val file = Gdx.files.internal(path)
        val contents = file.readString()
        val commands = contents.split("\\r?\\n")
        for (command in commands) {
            console.execCommand(command)
        }
        console.log("All commands from <$path> were executed.")
    }

    @ConsoleDoc(description = "Set fullscreen.")
    fun fullscreen() {
        val displayMode = Gdx.graphics.displayMode
        Gdx.graphics.setFullscreenMode(displayMode)
    }
}