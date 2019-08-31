package com.netrew;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raw.RawChel;
import com.netrew.ui.GameHud;
import com.netrew.ui.MainMenu;

import java.util.List;

public class Main extends Game {
	private SpriteBatch batch;
	private Texture img;
	private OrthographicCamera cam;
	public InputManager inputManager;
	public MainMenu menu;
	public GameHud hud;
	private InputMultiplexer inputs = new InputMultiplexer();
	public Stage stage;
	public Array<Chel> sprites;
	private BitmapFont font;
	public Skin skin;
	private Pixmap pixmap;
	Chel sprite;
	//
	public boolean newConnection = true;
	OrthogonalTiledMapRenderer renderer;
	AssetManager assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());

		cam = new OrthographicCamera();
		cam.viewportWidth = Gdx.graphics.getWidth();
		cam.viewportHeight = Gdx.graphics.getHeight();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);

		assets = new AssetManager();
		assets.load("circle.png", Texture.class);
		assets.load("fonts/ubuntu-16.fnt", BitmapFont.class);
		assets.load("DefaultSkin/uiskin.json", Skin.class);
		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assets.load("tilemap/untitled.tmx", TiledMap.class);
		assets.finishLoading();

		img = assets.get("circle.png");
		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		font = assets.get("fonts/ubuntu-16.fnt");
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		skin = assets.get("DefaultSkin/uiskin.json");
		pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		menu = new MainMenu(this);
		hud = new GameHud(this);
		setScreen(menu);

		inputManager = new InputManager(this, cam, sprites);
		inputs.addProcessor(inputManager);
		inputs.addProcessor(stage);
		Gdx.input.setInputProcessor(inputs);

		sprites = new Array<Chel>();

		sprite = new Chel("Name", img);
		sprite.getSprite().setPosition(0, 100);

		TiledMap map = assets.get("tilemap/untitled.tmx");
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
		layer.getCell(36, 35).setTile(layer.getCell(0, 0).getTile());
		float unitScale = 4f;
		renderer = new OrthogonalTiledMapRenderer(map, unitScale);
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0.4f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		batch.setProjectionMatrix(cam.combined);

		renderer.setView(cam);
		renderer.render();

		batch.begin();
		sprites.forEach(chel -> chel.update(batch, font));
		sprite.getSprite().draw(batch);
		sprite.getSprite().setPosition(sprite.getSprite().getX() + 20*dt, 0);
		batch.end();
		inputManager.handleInput(dt);
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		font.dispose();
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		stage.getViewport().update(width, height, true);
	}
}
