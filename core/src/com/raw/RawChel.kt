package com.raw

import java.io.Serializable

class RawChel(var name: String, var color: RawColor, var position: Coord) : Serializable {
    var target: Coord? = null
}
