/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netrew.game.pathfinding

import com.badlogic.gdx.ai.utils.Collision
import com.badlogic.gdx.ai.utils.Ray
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector
import com.badlogic.gdx.math.Vector2

/** A raycast collision detector used for path smoothing against a [TiledGraph].
 *
 * @param <N> Type of node, either flat or hierarchical, extending the [TiledNode] class
 *
 * @author davebaol
</N> */
class TiledRaycastCollisionDetector<N : TiledNode<N>?>(var worldMap: TiledGraph<N>) : RaycastCollisionDetector<Vector2> {

    // See http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    override fun collides(ray: Ray<Vector2>): Boolean {
        var x0 = ray.start.x.toInt()
        var y0 = ray.start.y.toInt()
        var x1 = ray.end.x.toInt()
        var y1 = ray.end.y.toInt()
        var tmp: Int
        val steep = Math.abs(y1 - y0) > Math.abs(x1 - x0)
        if (steep) {
            // Swap x0 and y0
            tmp = x0
            x0 = y0
            y0 = tmp
            // Swap x1 and y1
            tmp = x1
            x1 = y1
            y1 = tmp
        }
        if (x0 > x1) {
            // Swap x0 and x1
            tmp = x0
            x0 = x1
            x1 = tmp
            // Swap y0 and y1
            tmp = y0
            y0 = y1
            y1 = tmp
        }
        val deltax = x1 - x0
        val deltay = Math.abs(y1 - y0)
        var error = 0
        var y = y0
        val ystep = if (y0 < y1) 1 else -1
        for (x in x0..x1) {
            val tile: N = if (steep) worldMap.getNode(y, x) else worldMap.getNode(x, y)
            if (tile?.type !== TiledNode.TILE_FLOOR) return true // We've hit a wall
            error += deltay
            if (error + error >= deltax) {
                y += ystep
                error -= deltax
            }
        }
        return false
    }

    override fun findCollision(outputCollision: Collision<Vector2>, inputRay: Ray<Vector2>): Boolean {
        throw UnsupportedOperationException()
    }

    init {
        this.worldMap = worldMap
    }
}