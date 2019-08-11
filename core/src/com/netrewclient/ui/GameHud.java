package com.netrewclient.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.netrewclient.Main;

public class GameHud implements Screen {
	private Stage stage;
	private Skin skin;
	public Label characterLabel, chatLabel;
	public TextButton disconnectButton;
	private Main main;
	public TextField chatTextField;
	public boolean chat_enabled = true;

	public GameHud(Main main) {
		this.stage = main.stage;
		this.skin = main.skin;
		this.main = main;
	}

	@Override
	public void show() {
		characterLabel = new Label("Character is not selected", skin);
		characterLabel.setX(Gdx.graphics.getWidth() - characterLabel.getWidth());
		stage.addActor(characterLabel);

		chatLabel = new Label("Not connected", skin);
		chatLabel.setAlignment(Align.topLeft);
		chatLabel.setX(0);
		chatLabel.setY(Gdx.graphics.getHeight() - chatLabel.getHeight());
		stage.addActor(chatLabel);

		disconnectButton = new TextButton("Disconnect", skin);
		disconnectButton.setHeight(30);
		disconnectButton.setX(Gdx.graphics.getWidth() - disconnectButton.getWidth());
		disconnectButton.setY(Gdx.graphics.getHeight() - disconnectButton.getHeight());
		stage.addActor(disconnectButton);
		disconnectButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeListener.ChangeEvent event, Actor actor) {
				main.menu.connected = false;
				main.setScreen(main.menu);
				main.newConnection = true;
				main.rawchels = null;
				main.sprites.clear();
			}
		});

		chatTextField = new TextField("", skin);
		chatTextField.setWidth(Gdx.graphics.getWidth());
		chatTextField.setVisible(false);
		stage.addActor(chatTextField);
	}

	@Override
	public void render(float delta) {
		stage.draw();
		stage.act();
	}

	@Override
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
		characterLabel.remove();
		chatLabel.remove();
		disconnectButton.remove();
		chatTextField.remove();
	}

	@Override
	public void dispose() {

	}

	public void update() {
		// update chatLabel on enter & receive
	}
}
