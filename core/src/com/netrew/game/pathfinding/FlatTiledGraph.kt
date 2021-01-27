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
import com.netrew.game.pathfinding.TiledNode.Companion.TILE_TREE
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

        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                val color = Color(pixmap.getPixel(x, y))
                var type = TILE_WATER
                when (color) {
                    Color.BLACK -> type = TILE_WATER
                    Color.WHITE -> type = TILE_FLOOR
                    Color.GREEN -> type = TILE_TREE
                }
                nodes.add(FlatTiledNode(x, y, type, 4));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (x in 0 until sizeX) {
            val idx = x * sizeY
            for (y in 0 until sizeY) {
                val node = nodes[idx + y]
                node.connections.clear()
                if (x > 0)
                    addConnection(node, -1, 0)
                if (y > 0)
                    addConnection(node, 0, -1)
                if (x < sizeX - 1)
                    addConnection(node, 1, 0)
                if (y < sizeY - 1)
                    addConnection(node, 0, 1)
            }
        }
    }

    fun setNodeType(x: Int, y: Int, type: Int) {
        val node = get(x, y)
        node.type = type
        node.connections.clear()
        if (type == TILE_FLOOR) {
            if (x > 0)
                addConnection(node, -1, 0)
            if (y > 0)
                addConnection(node, 0, -1)
            if (x < sizeX - 1)
                addConnection(node, 1, 0)
            if (y < sizeY - 1)
                addConnection(node, 0, 1)
        }
    }

    operator fun get(x: Int, y: Int): FlatTiledNode = getNode(x, y)

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

    fun getNodeByPosition(position: Vector2, tileSize: Float): FlatTiledNode {
        val x = (position.x / tileSize).toInt()
        val y = (position.y / tileSize).toInt()
        return getNode(x, y)
    }
}