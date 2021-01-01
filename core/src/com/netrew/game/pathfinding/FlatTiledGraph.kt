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

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.netrew.game.pathfinding.TiledNode.Companion.TILE_FLOOR
import com.netrew.game.pathfinding.TiledNode.Companion.TILE_WATER
/** A random generated graph representing a flat tiled map.
 *
 * @author davebaol
 */
class FlatTiledGraph : TiledGraph<FlatTiledNode?> {
    protected var nodes: Array<FlatTiledNode>
    var diagonal: Boolean
    var startNode: FlatTiledNode?
    companion object {
        var sizeX = 0
        var sizeY = 0
    }

    override fun init(pixmap: Pixmap) {
        sizeX = pixmap.width
        sizeY = pixmap.height

        val flipped = Pixmap(sizeX, sizeY, Pixmap.Format.RGBA8888)
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                flipped.drawPixel(x, y, pixmap.getPixel(x, sizeY - 1 - y))
            }
        }

        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                val color = Color(flipped.getPixel(x, y))
                var type = TILE_WATER
                when (color) {
                    Color.BLACK -> type = TILE_WATER
                    Color.WHITE -> type = TILE_FLOOR
                }
                nodes.add(FlatTiledNode(x, y, type, 4));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (x in 0 until sizeX) {
            val idx = x * sizeY
            for (y in 0 until sizeY) {
                val n = nodes[idx + y]
                if (x > 0)
                    addConnection(n, -1, 0)
                if (y > 0)
                    addConnection(n, 0, -1)
                if (x < sizeX - 1)
                    addConnection(n, 1, 0)
                if (y < sizeY - 1)
                    addConnection(n, 0, 1)
            }
        }
    }

    override fun getNode(x: Int, y: Int): FlatTiledNode {
        return nodes[x * sizeY + y]
    }

    override fun getNode(index: Int): FlatTiledNode {
        return nodes[index]
    }

    override fun getNodeCount(): Int {
        return nodes.size
    }

    private fun addConnection(n: FlatTiledNode, xOffset: Int, yOffset: Int) {
        val target = getNode(n.x + xOffset, n.y + yOffset)
        if (target.type == TILE_FLOOR)
            n.connections.add(FlatTiledConnection(this, n, target))
    }

    override fun getConnections(fromNode: FlatTiledNode?): Array<Connection<FlatTiledNode?>> {
        return fromNode!!.connections
    }

    init {
        nodes = Array(sizeX * sizeY)
        diagonal = false
        startNode = null
    }

    override fun getIndex(node: FlatTiledNode?): Int {
        return node!!.index
    }

    fun getNodeByPosition(position: Vector2, tileSize: Int, scale: Float): FlatTiledNode {
        val x = (position.x / (tileSize * scale)).toInt()
        val y = (position.y / (tileSize * scale)).toInt()
        return getNode(x, y)
    }
}