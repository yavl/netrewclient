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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.netrew.ui.GameHud
import com.netrew.ui.MainMenu

import java.util.ArrayList
import java.util.function.Predicate

class Main : Game() {
    lateinit private var batch: SpriteBatch
    lateinit private var img: Texture
    private var cam: OrthographicCamera = OrthographicCamera();
    lateinit var inputManager: InputManager
    lateinit var menu: MainMenu
    lateinit var hud: GameHud
    private val inputs = InputMultiplexer()
    lateinit var stage: Stage
    lateinit var sprites: Array<Chel>
    lateinit private var font: BitmapFont
    lateinit var skin: Skin
    lateinit internal var sprite: Chel
    //
    lateinit internal var renderer: OrthogonalTiledMapRenderer
    lateinit internal var assets: AssetManager

    override fun create() {
        initAssets()
        batch = SpriteBatch()
        stage = Stage(ScreenViewport())
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
        inputs.addProcessor(stage)
        Gdx.input.inputProcessor = inputs

        sprite = Chel("Name", img)
        sprite.sprite.setPosition(0f, 100f)

        val map = assets.get<TiledMap>("tilemap/untitled.tmx")
        val unitScale = 4f
        renderer = OrthogonalTiledMapRenderer(map, unitScale)
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
        sprite.sprite.draw(batch)
        sprite.sprite.setPosition(sprite.sprite.x + 20 * dt, 0f)
        batch.end()
        inputManager.handleInput(dt)
        super.render()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
        font.dispose()
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
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
