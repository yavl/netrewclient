package com.netrewclient.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.netrewclient.Client;
import com.netrewclient.Main;

public class MainMenu implements Screen {
	private Stage stage;
	private Skin skin;
	private TextField nameTextField;
	private TextField ipTextField;
	private TextButton connectButton;
	private Main main;
	public boolean connected;
	public Client client;
	public boolean updatechels;

	public MainMenu(Main main) {
		this.main = main;
		this.stage = main.stage;
		this.skin = main.skin;
	}

	@Override
	public void show() {
		ipTextField = new TextField("127.0.0.1", skin);
		stage.addActor(ipTextField);

		nameTextField = new TextField("default_name", skin);
		nameTextField.setY(ipTextField.getY() + ipTextField.getHeight());
		stage.addActor(nameTextField);

		connectButton = new TextButton("Connect", skin);
		connectButton.setX(ipTextField.getX() + ipTextField.getWidth());
		connectButton.setHeight(30);
		stage.addActor(connectButton);
		connectButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeListener.ChangeEvent event, Actor actor) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						connected = true;
						client = new Client(nameTextField.getText(), ipTextField.getText(), 13370, main);
						client.start();
					}
				};
				new Thread(r).start();
				main.setScreen(main.hud);
			}
		});
	}

	@Override
	public void render(float delta) {
		stage.draw();
		stage.act();
	}

	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		nameTextField.remove();
		ipTextField.remove();
		connectButton.remove();
	}

	@Override
	public void dispose() {
	}
}
