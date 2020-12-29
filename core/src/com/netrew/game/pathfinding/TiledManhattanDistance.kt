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

import com.badlogic.gdx.ai.pfa.Heuristic
import kotlin.math.abs

/** A Manhattan distance heuristic for a [TiledGraph]. It simply calculates the Manhattan distance between two given
 * tiles.
 *
 * @param <N> Type of node, either flat or hierarchical, extending the [TiledNode] class
 *
 * @author davebaol
</N> */
class TiledManhattanDistance<N : TiledNode<N>?> : Heuristic<N> {
    override fun estimate(node: N, endNode: N): Float {
        return abs(endNode?.x!! - node?.x!!).toFloat() + abs(endNode.y - node.y)
    }
}