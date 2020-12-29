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

import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath
import com.badlogic.gdx.math.Vector2

/** A smoothable path for a generic tiled graph.
 *
 * @param <N> Type of node, either flat or hierarchical, extending the [TiledNode] class
 *
 * @author davebaol
</N> */
class TiledSmoothableGraphPath<N : TiledNode<N>?> : DefaultGraphPath<N>(), SmoothableGraphPath<N, Vector2> {
    private val tmpPosition = Vector2()

    /** Returns the position of the node at the given index.
     *
     *
     * **Note that the same Vector2 instance is returned each time this method is called.**
     * @param index the index of the node you want to know the position
     */
    override fun getNodePosition(index: Int): Vector2 {
        val node = nodes[index]
        return tmpPosition.set(node?.x!!.toFloat(), node.y.toFloat())
    }

    override fun swapNodes(index1: Int, index2: Int) {
// x.swap(index1, index2);
// y.swap(index1, index2);
        nodes[index1] = nodes[index2]
    }

    override fun truncatePath(newLength: Int) {
        nodes.truncate(newLength)
    }
}