package com.netrew

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.netrew.entities.ChelView
import com.netrew.ui.GameHud
import com.netrew.ui.MainMenu
import ktx.actors.onClick
import java.io.StringWriter

class Main : Game() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private var cam: OrthographicCamera = OrthographicCamera();
    lateinit var inputManager: InputManager
    lateinit var menu: MainMenu
    lateinit var hud: GameHud
    private val inputs = InputMultiplexer()
    lateinit var uiStage: Stage
    lateinit var sprites: Array<ChelView>
    lateinit private var font: BitmapFont
    lateinit var skin: Skin
    lateinit internal var sprite: ChelView
    //
    lateinit internal var renderer: OrthogonalTiledMapRenderer
    lateinit internal var assets: AssetManager
    lateinit var imageActor: Image
    lateinit var stage: Stage

    override fun create() {
        initAssets()
        batch = SpriteBatch()
        uiStage = Stage(ScreenViewport())
        cam.viewportWidth = Gdx.graphics.width.toFloat()
        cam.viewportHeight = Gdx.graphics.height.toFloat()
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0f)

        img = assets.get<Texture>("circle.png")
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        font = assets.get<BitmapFont>("fonts/ubuntu-16.fnt")
        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        skin = assets.get("DefaultSkin/uiskin.json")
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        skin.add("white", Texture(pixmap))

        menu = MainMenu(this)
        hud = GameHud(this)
        setScreen(menu)

        sprites = Array()

        inputManager = InputManager(this, cam, sprites)
        inputs.addProcessor(inputManager)
        inputs.addProcessor(uiStage)
        Gdx.input.inputProcessor = inputs

        val viewp = ScreenViewport(cam)
        stage = Stage(viewp, batch)
        stage.isDebugAll = true

        sprites.add(ChelView("John", img, stage))
        sprites.get(0).sprite.setPosition(5165f, 5150f)
        sprites.add(ChelView("Alisa", img, stage))
        sprites.get(1).sprite.setPosition(5046f, 5371f)
        sprites.add(ChelView("George", img, stage))
        sprites.get(2).sprite.setPosition(5173f, 5379f)
        sprites.add(ChelView("Paul", img, stage))
        sprites.get(3).sprite.setPosition(5134f, 5269f)
        sprites.forEach { chel -> chel.sprite.setColor(245f / 255f, 208f / 255f, 141f / 255f, 255f / 255f) }


        sprites.add(ChelView("Gosha", img, stage))
        sprites.get(4).sprite.setPosition(6417f, 6790f)
        sprites.add(ChelView("Dasha", img, stage))
        sprites.get(5).sprite.setPosition(4871f, 4794f)
        sprites.add(ChelView("Grisha", img, stage))
        sprites.get(6).sprite.setPosition(4842f, 4970f)
        sprites.add(ChelView("Rita", img, stage))
        sprites.get(7).sprite.setPosition(4715f, 4886f)
        sprites.add(ChelView("Senorita", img, stage))
        sprites.get(8).sprite.setPosition(4824f, 4904f)
        for (i in 4..8) {
            sprites.get(i).sprite.setColor(139f / 255f, 99f / 255f, 31 / 255f, 255f / 255f)
        }

        val map = assets.get<TiledMap>("tilemap/untitled.tmx")
        val unitScale = 4f
        renderer = OrthogonalTiledMapRenderer(map, unitScale)

        val file = Gdx.files.local("config.json")
        val writer = StringWriter()
        val json = Json(JsonWriter.OutputType.json)
        json.setWriter(writer)
        json.writeObjectStart()
        json.writeValue("cameraSpeed", 500f)
        json.writeValue("cameraZoomFactor", 0.15f)
        json.writeObjectEnd()
        file.writeString(json.prettyPrint(json.writer.writer.toString()), false)

        imageActor = Image(img)
        stage.addActor(imageActor)
        inputs.addProcessor(stage)

        // libKTX:
        imageActor.onClick { println("hello click") }

        // без libKTX:
        imageActor.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                println("hello click")
            }
        })
    }

    override fun render() {
        val dt = Gdx.graphics.deltaTime
        Gdx.gl.glClearColor(0f, 0.4f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cam.update()
        batch.projectionMatrix = cam.combined

        renderer.setView(cam)
        renderer.render()

        batch.begin()
        sprites.forEach { chel -> chel.update(batch, font) }
        sprites.forEach { chel -> chel.sprite.draw(batch, 1f) }
        batch.end()
        stage.act()
        stage.draw()
        inputManager.handleInput(dt)
        super.render()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
        font.dispose()
        uiStage.dispose()
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        uiStage.viewport.update(width, height, true)
        stage.viewport.update(width, height, true)
    }

    private fun initAssets() {
        assets = AssetManager()
        assets.load("circle.png", Texture::class.java)
        assets.load("fonts/ubuntu-16.fnt", BitmapFont::class.java)
        assets.load("DefaultSkin/uiskin.json", Skin::class.java)
        assets.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        assets.load("tilemap/untitled.tmx", TiledMap::class.java)
        assets.finishLoading()
    }
}
