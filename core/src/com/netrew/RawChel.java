package com.netrew;

import java.io.Serializable;

public class RawChel implements Serializable {
	public String name;
	public RawColor color;
	public Coord position;
	public Coord target;

	public RawChel(String name, RawColor color, Coord position) {
		this.name = name;
		this.color = color;
		this.position = position;
	}
}
