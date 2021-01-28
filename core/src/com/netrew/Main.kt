package com.netrew

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.netrew.Globals.uiStage
import com.netrew.game.GameSaver
import com.netrew.game.World
import com.netrew.game.systems.*
import com.netrew.ui.MainMenu
import ktx.scene2d.Scene2DSkin

class Main : Game() {
    private lateinit var batch: SpriteBatch
    private lateinit var inputManager: InputManager
    private lateinit var menu: MainMenu
    private val inputs = InputMultiplexer()
    private lateinit var assets: AssetManager

    private val engine = PooledEngine()
    private val cam = Globals.cam

    override fun create() {
        initAssets()
        VisUI.load()
        batch = SpriteBatch()
        uiStage = Stage(ScreenViewport())
        cam.viewportWidth = Gdx.graphics.width.toFloat()
        cam.viewportHeight = Gdx.graphics.height.toFloat()
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0f)

        val viewport = ScreenViewport(cam)
        Globals.createStage(viewport, batch)
        val stage = Globals.stage

        inputManager = InputManager()
        inputs.addProcessor(inputManager)
        inputs.addProcessor(uiStage)
        inputs.addProcessor(stage)
        Gdx.input.inputProcessor = inputs

        Globals.generateFonts()
        Globals.bundle = assets.get("languages/bundle")
        Globals.skin = VisUI.getSkin()
        Globals.skin.add("default-font", Globals.Fonts.defaultFont)
        Globals.world = World(engine)
        Scene2DSkin.defaultSkin = Globals.skin

        menu = MainMenu()
        setScreen(menu)

        engine.addSystem(MovementSystem())
        engine.addSystem(StageRenderingSystem(stage, 0))
        engine.addSystem(BordersDrawingSystem())
        engine.addSystem(SpriteRenderingSystem())
        engine.addSystem(HouseSpriteRenderingSystem())
        engine.addSystem(TreeSpriteRenderingSystem())
        engine.addSystem(NameLabelRenderingSystem())
        engine.addSystem(TreeCuttingSystem())
        Globals.world.create()

        Globals.createConsole()
        Globals.console.execCommand("exec autoexec.cfg")
        val gameSaver = GameSaver()
        gameSaver.loadSettings()
    }

    override fun render() {
        Gdx.gl.glClearColor(68 / 255f, 121 / 255f, 163f / 255f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cam.update()
        batch.projectionMatrix = cam.combined

        val dt = Gdx.graphics.deltaTime
        inputManager.handleInput(dt)
        engine.update(dt)
        Globals.world.update(dt)
        super.render()

        Globals.console.draw()
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
        uiStage.dispose()
        menu.dispose()
        Globals.dispose()
    }

    override fun resize(width: Int, height: Int) {
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        uiStage.viewport.update(width, height, true)
        Globals.stage.viewport.update(width, height)
        Globals.console.window.stage.viewport.update(width, height)
        menu.resize(width, height)
    }

    private fun initAssets() {
        assets = Globals.assets
        assets.load("gfx/circle.png", Texture::class.java)
        assets.load("gfx/tree.png", Texture::class.java)
        assets.load("gfx/house.png", Texture::class.java)
        assets.load("skins/uiskin.json", Skin::class.java)
        assets.load("languages/bundle", I18NBundle::class.java)
        assets.finishLoading()
    }

    override fun pause() {
        Globals.timeScale = 0f
    }

    override fun resume() {
        Globals.timeScale = Globals.DEFAULT_TIMESCALE
    }
}