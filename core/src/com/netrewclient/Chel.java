package com.netrewclient;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.netrew.RawChel;
import com.netrew.RawColor;

public class Chel {
	public String name;
	public Vector2 target;
	private Sprite sprite;
	private int srcX = 0, srcY = 0;
	private int srcWidth = 32, srcHeight = 32;
	public RawChel rawchel;

	public Chel(String name, Texture tex) {
		this.name = name;
		sprite = new Sprite();
		sprite.setTexture(tex);
		sprite.setColor(1, 1, 1, 1);
		sprite.setRegion(srcX, srcY, srcWidth, srcHeight);
		sprite.setSize(Math.abs(srcWidth), Math.abs(srcHeight));
		sprite.setOrigin(tex.getWidth() / 2.0f, tex.getWidth() / 2.0f);
	}

	public Chel(RawChel rawch, Texture tex) {
		this.rawchel = rawch;
		this.name = rawch.name;
		RawColor c = rawch.color;
		sprite = new Sprite();
		sprite.setColor(c.r, c.g, c.b, c.a);

		sprite.setTexture(tex);
		sprite.setRegion(srcX, srcY, srcWidth, srcHeight);
		sprite.setSize(Math.abs(srcWidth), Math.abs(srcHeight));
		sprite.setOrigin(tex.getWidth() / 2.0f, tex.getWidth() / 2.0f);
		sprite.setOriginBasedPosition(rawch.position.x, rawch.position.y);
	}

	public void update(SpriteBatch batch, BitmapFont font) {
		sprite.draw(batch);
		float x = Interpolation.linear.apply(sprite.getX(), rawchel.position.x, 1);
		float y = Interpolation.linear.apply(sprite.getY(), rawchel.position.y, 1);
		sprite.setOriginBasedPosition(x, y);
		font.draw(batch, this.name, sprite.getX(), sprite.getY() - 5);
	}

	public Sprite getSprite() {
		return sprite;
	}
}
