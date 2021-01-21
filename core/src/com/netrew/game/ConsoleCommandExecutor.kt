package com.netrew.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.netrew.Globals
import com.netrew.Mediator
import com.netrew.toWorldPos
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import java.lang.Exception
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ConsoleCommandExecutor(private val mediator: Mediator) : CommandExecutor() {
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
        mediator.timescale(scale)
        console.log("Timescale is set to ${scale}x")
    }

    @ConsoleDoc(description = "Set camera position (x, y).")
    fun cam(x: Float, y: Float) {
        mediator.camera().position.set(x, y, 0f)
    }

    @ConsoleDoc(description = "Set camera zoom.")
    fun zoom(zoom: Float) {
        mediator.camera().zoom = zoom
    }

    @ConsoleDoc(description = "Show debug info.")
    fun debug(enabled: Boolean) {
        mediator.stage().isDebugAll = enabled
        mediator.uiStage().isDebugAll = enabled
    }

    @ConsoleDoc(description = "Show version info.")
    fun version() {
        console.log(mediator.version())
    }

    @ConsoleDoc(description = "Show cursor pos (x, y).")
    fun cursor() {
        console.log("${Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos()}")
    }

    @ConsoleDoc(description = "Spawn character at cursor pos.")
    fun spawn() {
        mediator.world().createCharacter(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos())
    }

    @ConsoleDoc(description = "Spawn tree at cursor pos.")
    fun spawntree() {
        mediator.world().createTree(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).toWorldPos())
    }

    @ConsoleDoc(description = "Save game.")
    fun save() {
        mediator.world().saveGame()
    }

    @ConsoleDoc(description = "Load game.")
    fun load() {
        mediator.world().loadGame()
    }

    @ConsoleDoc(description = "Remove all characters.")
    fun clear() {
        mediator.world().clearCharacters()
    }
}