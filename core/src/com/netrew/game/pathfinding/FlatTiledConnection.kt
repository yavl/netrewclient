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

import com.badlogic.gdx.ai.pfa.DefaultConnection

/** A connection for a [FlatTiledGraph].
 *
 * @author davebaol
 */
class FlatTiledConnection(var worldMap: FlatTiledGraph, fromNode: FlatTiledNode?, toNode: FlatTiledNode?) : DefaultConnection<FlatTiledNode>(fromNode, toNode) {
    override fun getCost(): Float {
        if (worldMap.diagonal)
            return 1f
        if (getToNode().x != worldMap.startNode?.x && getToNode().y != worldMap.startNode?.y)
            return NON_DIAGONAL_COST
        return 1f
    }

    companion object {
        val NON_DIAGONAL_COST = Math.sqrt(2.0).toFloat()
    }
}