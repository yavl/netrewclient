package com.netrewclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.netrew.Request;

public class InputManager implements InputProcessor {
	private OrthographicCamera cam;
	private float camSpeed = 500.0f;
	private Vector2 dragOld = new Vector2();
	private Vector2 dragNew = new Vector2();
	public Chel selected;
	private Array<Chel> sprites;
	private Main main;
	boolean send_message = false;

	public InputManager(Main main, OrthographicCamera cam, Array<Chel> sprites) {
		this.main = main;
		this.cam = cam;
		this.sprites = sprites;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (main.getScreen() != main.menu) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if (button == Input.Buttons.LEFT) {
				for (Chel chel : sprites) {
					Vector3 input = new Vector3(x, y, 0);
					cam.unproject(input);
					if (chel.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
						selected = chel;
						main.hud.characterLabel.setText(selected.name);
						break;
					} else {
						selected = null;
						main.hud.characterLabel.setText("Character is not selected");
					}
				}
			}
			if ((button == Input.Buttons.RIGHT) && (selected != null)) {
				Vector3 input = new Vector3(x, y, 0);
				cam.unproject(input);
				selected.target = new Vector2(input.x, input.y);
				main.menu.client.requests.sendRequest(Request.SERVER_RECEIVE_TARGET);
				main.menu.client.sendLine(selected.name);
				main.menu.client.sendVector2(selected.target);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		switch (amount) {
			case 1:
				cam.zoom += 0.15f * cam.zoom;
				break;
			case -1:
				cam.zoom -= 0.15f * cam.zoom;
				break;
		}
		return false;
	}

	public void handleInput(float dt) {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.translate(-camSpeed * cam.zoom * dt, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.translate(camSpeed * cam.zoom * dt, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.translate(0, -camSpeed * cam.zoom * dt, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.translate(0, camSpeed * cam.zoom * dt, 0);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if ((Gdx.input.isKeyJustPressed(Input.Keys.Y)) && (main.getScreen() == main.hud)) {
			main.hud.chatTextField.setVisible(true);
			main.stage.setKeyboardFocus(main.hud.chatTextField);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && main.getScreen() == main.hud) {
			if (main.hud.chatTextField.getText().length() > 0) {
				send_message = true;
			} else {
				main.hud.chatTextField.setVisible(false);
			}
			main.stage.unfocusAll();
		}

		if (Gdx.input.justTouched()) {
			dragNew.set(Gdx.input.getX(), Gdx.input.getY());
			dragOld.set(dragNew);
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
			dragNew.set(Gdx.input.getX(), Gdx.input.getY());
			if (!dragNew.equals(dragOld)) {
				cam.translate((dragOld.x - dragNew.x) * cam.zoom, (dragNew.y - dragOld.y) * cam.zoom);
				dragOld.set(dragNew);
			}
		}
	}
}
