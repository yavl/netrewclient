package com.netrew

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.netrew.Globals.uiStage
import com.netrew.game.ConsoleCommandExecutor
import com.netrew.game.World
import com.netrew.game.systems.*
import com.netrew.ui.MainMenu
import ktx.scene2d.Scene2DSkin

class Main : Game() {
    private lateinit var batch: SpriteBatch
    lateinit var inputManager: InputManager
    lateinit var menu: MainMenu
    private val inputs = InputMultiplexer()
    //
    lateinit internal var assets: AssetManager

    val engine = PooledEngine()
    val mediator = Mediator()
    val cam = mediator.camera()

    override fun create() {
        initAssets()
        VisUI.load()
        batch = SpriteBatch()
        uiStage = Stage(ScreenViewport())
        cam.viewportWidth = Gdx.graphics.width.toFloat()
        cam.viewportHeight = Gdx.graphics.height.toFloat()
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0f)

        Globals.defaultFont = generateFont(24)
        Globals.chatFont = generateFont(20)
        Globals.characterFont = generateFont(20)
        Globals.bundle = assets.get<I18NBundle>("languages/bundle")
        Globals.skin = VisUI.getSkin()
        Globals.skin.add("default-font", Globals.defaultFont)
        Globals.world = World(mediator, engine)
        Scene2DSkin.defaultSkin = Globals.skin

        menu = MainMenu(this, mediator)
        setScreen(menu)

        val viewp = ScreenViewport(cam)
        mediator.createStage(viewp, batch)
        val stage = mediator.stage()
        stage.isDebugAll = true

        inputManager = InputManager(mediator)
        inputs.addProcessor(inputManager)
        inputs.addProcessor(uiStage)
        inputs.addProcessor(stage)
        Gdx.input.inputProcessor = inputs

        val prefs = Gdx.app.getPreferences("NetrewPreferences")
        val x = prefs.getFloat("cameraPosX", 0f)
        val y = prefs.getFloat("cameraPosY", 0f)
        val zoom = prefs.getFloat("cameraZoom", 1f)
        cam.position.set(x, y, 0f)
        cam.zoom = zoom

        mediator.createConsole()
        val console = mediator.console()
        console.setCommandExecutor(ConsoleCommandExecutor(mediator))
        console.setTitle("")
        console.enableSubmitButton(true)
        console.window.isMovable = false
        val consoleBgPixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888);
        consoleBgPixmap.setColor(Color(0f, 0f, 0f, 0.6f));
        consoleBgPixmap.fill();
        val consoleBg = TextureRegionDrawable(TextureRegion(Texture(consoleBgPixmap)));
        console.window.setBackground(consoleBg)
        console.setSizePercent(100f, 50f)
        console.isVisible = false

        engine.addSystem(MovementSystem())
        engine.addSystem(StageRenderingSystem(stage, 0))
        engine.addSystem(TerritoryRenderingSystem())
        engine.addSystem(SpriteRenderingSystem())
        engine.addSystem(HouseSpriteRenderingSystem())
        engine.addSystem(TreeSpriteRenderingSystem())
        engine.addSystem(NameLabelRenderingSystem())
        mediator.world().create()
    }

    override fun render() {
        Gdx.gl.glClearColor(68 / 255f, 121 / 255f, 163f / 255f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cam.update()
        batch.projectionMatrix = cam.combined

        val dt = Gdx.graphics.deltaTime
        engine.update(dt)
        inputManager.handleInput(dt)
        super.render()

        mediator.console().draw()
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
        uiStage.dispose()
        mediator.dispose()
    }

    override fun resize(width: Int, height: Int) {
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        uiStage.viewport.update(width, height, true)
        mediator.stage().viewport.update(width, height)
        mediator.console().window.stage.viewport.update(width, height)
        menu.resize(width, height)
    }

    private fun initAssets() {
        assets = mediator.assets()
        assets.load("gfx/circle.png", Texture::class.java)
        assets.load("gfx/tree.png", Texture::class.java)
        assets.load("gfx/house.png", Texture::class.java)
        assets.load("skins/uiskin.json", Skin::class.java)
        assets.load("languages/bundle", I18NBundle::class.java)
        assets.load("maps/europe/heightmap.png", Texture::class.java)
        assets.load("maps/europe/terrain.png", Texture::class.java)
        assets.finishLoading()
    }

    private fun generateFont(size: Int): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Ubuntu-Regular.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = size
        parameter.magFilter = Texture.TextureFilter.Linear
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        return generator.generateFont(parameter)
    }

    override fun pause() {
        mediator.timescale(0f)
    }

    override fun resume() {
        mediator.timescale(Globals.DEFAULT_TIMESCALE)
    }
}