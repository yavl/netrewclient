package com.raw;

import java.io.Serializable;

public class RawColor implements Serializable {
	public float r, g, b, a;

	public RawColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public static RawColor averageColor(RawColor one, RawColor two) {
		float rr = (float) Math.sqrt((one.r * one.r + two.r * two.r) / 2);
		float gg = (float) Math.sqrt((one.g * one.g + two.g * two.g) / 2);
		float bb = (float) Math.sqrt((one.b * one.b + two.b * two.b) / 2);
		return new RawColor(rr, gg, bb, 1.0f);
	}
}
