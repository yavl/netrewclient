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
import com.badlogic.gdx.utils.Array

/** A node for a [TiledGraph].
 *
 * @param <N> Type of node, either flat or hierarchical, extending the [TiledNode] class
 *
 * @author davebaol
</N> */
abstract class TiledNode<N : TiledNode<N>?>(
    /** The x coordinate of this tile  */
        val x: Int,
    /** The y coordinate of this tile  */
        val y: Int,
    /** The type of this tile, see [.TILE_EMPTY], [.TILE_FLOOR] and [.TILE_WALL]  */
        var type: Int,
    var connections: Array<Connection<FlatTiledNode?>>) {
    abstract val index: Int

    companion object {
        /// A constant representing a water tile
        const val TILE_WATER = 0

        /// A constant representing a walkable tile
        const val TILE_FLOOR = 1

        /// A constant representing a tree
        const val TILE_TREE = 2

        /// A constant representing a field where plants be able to grow
        const val TILE_FIELD = 3

        /// A constant representing a building
        const val TILE_BUILDING = 4
    }
}