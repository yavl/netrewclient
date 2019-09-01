package com.raw

import java.io.Serializable

class RawColor(var r: Float, var g: Float, var b: Float, var a: Float) : Serializable {
    companion object {

        fun averageColor(one: RawColor, two: RawColor): RawColor {
            val rr = Math.sqrt(((one.r * one.r + two.r * two.r) / 2).toDouble()).toFloat()
            val gg = Math.sqrt(((one.g * one.g + two.g * two.g) / 2).toDouble()).toFloat()
            val bb = Math.sqrt(((one.b * one.b + two.b * two.b) / 2).toDouble()).toFloat()
            return RawColor(rr, gg, bb, 1.0f)
        }
    }
}
