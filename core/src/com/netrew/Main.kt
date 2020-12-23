package com.netrew

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.netrew.Globals.uiStage
import com.netrew.game.ConsoleCommandExecutor
import com.netrew.game.TilemapEntityListener
import com.netrew.game.World
import com.netrew.game.components.TilemapComponent
import com.netrew.game.systems.*
import com.netrew.ui.MainMenu
import com.strongjoshua.console.CommandExecutor
import com.strongjoshua.console.annotation.ConsoleDoc
import ktx.scene2d.Scene2DSkin
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class Main : Game() {
    private lateinit var batch: SpriteBatch
    lateinit var inputManager: InputManager
    lateinit var menu: MainMenu
    private val inputs = InputMultiplexer()
    lateinit private var font: BitmapFont
    //
    lateinit internal var assets: AssetManager

    val engine = PooledEngine()
    val mediator = Mediator()
    val world = World(mediator, engine)
    val cam = mediator.camera()
    lateinit var fbo: FrameBuffer

    override fun create() {
        initAssets()
        batch = SpriteBatch()
        uiStage = Stage(ScreenViewport())
        cam.viewportWidth = Gdx.graphics.width.toFloat()
        cam.viewportHeight = Gdx.graphics.height.toFloat()
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0f)

        Globals.bundle = assets.get<I18NBundle>("languages/bundle")

        font = assets.get<BitmapFont>("fonts/ubuntu-16.fnt")
        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        Globals.skin = assets.get("skins/uiskin.json")
        Globals.defaultFont = generateFont(24)
        Globals.chatFont = generateFont(20)
        Scene2DSkin.defaultSkin = Globals.skin
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        Globals.skin.add("white", Texture(pixmap))

        menu = MainMenu(this, mediator)
        //hud = GameHud(this, mediator)
        setScreen(menu)

        inputManager = InputManager(this, cam)
        inputs.addProcessor(inputManager)
        inputs.addProcessor(uiStage)
        Gdx.input.inputProcessor = inputs

        val viewp = ScreenViewport(cam)
        mediator.createStage(viewp, batch)
        val stage = mediator.stage()
        stage.isDebugAll = true

        val prefs = Gdx.app.getPreferences("NetrewPreferences")
        val x = prefs.getFloat("cameraPosX", 0f)
        val y = prefs.getFloat("cameraPosY", 0f)
        val zoom = prefs.getFloat("cameraZoom", 1f)
        cam.position.set(x, y, 0f)
        cam.zoom = zoom

        inputs.addProcessor(stage)

        engine.addSystem(MovementSystem())
        engine.addSystem(TilemapRenderingSystem(mediator))
        engine.addSystem(StageRenderingSystem(stage, 0))
        engine.addSystem(SpriteRenderingSystem())
        engine.addSystem(NameLabelRenderingSystem())
        engine.addEntityListener(Family.all(TilemapComponent::class.java).get(), TilemapEntityListener())
        world.create()

        fbo = FrameBuffer(Pixmap.Format.RGBA8888, 1600, 1200, false, false)
        renderFbo()

        mediator.createConsole()
        val console = mediator.console()
        console.setCommandExecutor(ConsoleCommandExecutor(mediator))
        console.setTitle("")
        console.enableSubmitButton(true)
        console.window.isMovable = false
        val pixmapa = Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapa.setColor(Color(0f, 0f, 0f, 0.6f));
        pixmapa.fill();
        val drawableBg = TextureRegionDrawable(TextureRegion(Texture(pixmapa)));
        console.window.setBackground(drawableBg)
        console.setSizePercent(100f, 50f)
        console.isVisible = false
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0.4f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cam.update()
        batch.projectionMatrix = cam.combined

        val dt = Gdx.graphics.deltaTime
        engine.update(dt)
        inputManager.handleInput(dt)

        super.render()
        mediator.console().draw()
        renderFbo()
        batch.begin()
        batch.draw(fbo.colorBufferTexture, 0f, 0f, 2048f, 2048f)
        batch.end()
    }

    fun renderFbo() {
        fbo.begin()
        batch.begin()
        Gdx.gl.glClearColor(0f, 0.4f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.draw(assets.get<Texture>("circle.png"), 1024f, 1024f, 64f, 64f)
        batch.end()
        fbo.end()
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
        font.dispose()
        uiStage.dispose()
        mediator.dispose()
    }

    override fun resize(width: Int, height: Int) {
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        uiStage.viewport.update(width, height, true)
        mediator.stage().viewport.update(width, height)
        menu.resize(width, height)
    }

    private fun initAssets() {
        assets = mediator.assets()
        assets.load("circle.png", Texture::class.java)
        assets.load("fonts/ubuntu-16.fnt", BitmapFont::class.java)
        assets.load("skins/uiskin.json", Skin::class.java)
        assets.load("languages/bundle", I18NBundle::class.java)
        assets.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        assets.load("tilemap/untitled.tmx", TiledMap::class.java)
        assets.finishLoading()
    }

    private fun generateFont(size: Int): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.local("fonts/Ubuntu-Regular.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = size
        parameter.magFilter = Texture.TextureFilter.Linear
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        return generator.generateFont(parameter)
    }
}