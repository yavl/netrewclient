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

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.graphics.Pixmap

/** Graph interface representing a generic tiled map.
 *
 * @param <N> Type of node, either flat or hierarchical, extending the [TiledNode] class
 *
 * @author davebaol
</N> */
interface TiledGraph<N : TiledNode<N>?> : IndexedGraph<N> {
    fun init(pixmap: Pixmap)
    fun getNode(x: Int, y: Int): N
    fun getNode(index: Int): N
}