package com.netrew.game

import com.netrew.Mediator
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ConsoleCommandExecutor(private val mediator: Mediator) : CommandExecutor() {
    @ConsoleDoc(description = "Shows time in system time zone.")
    fun time() {
        console.log("Current time: " + DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.now()).toString())
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
}