package com.netrew.game.pathfinding

import com.badlogic.gdx.ai.pfa.PathSmoother
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder
import com.badlogic.gdx.math.Vector2

/** Wrapper class around gdx-ai pathfinding */
class Pathfinder(worldMap: FlatTiledGraph) {
    val path = TiledSmoothableGraphPath<FlatTiledNode>()
    private val pathfinder = IndexedAStarPathFinder<FlatTiledNode>(worldMap, true)
    private val pathSmoother = PathSmoother<FlatTiledNode, Vector2>(TiledRaycastCollisionDetector<FlatTiledNode?>(worldMap))
    private val heuristic = TiledManhattanDistance<FlatTiledNode>()

    /** Find path between startNode and endNode */
    fun searchPath(startNode: FlatTiledNode, endNode: FlatTiledNode): Boolean {
        path.clear()
        val found = pathfinder.searchNodePath(startNode, endNode, heuristic, path)
        pathSmoother.smoothPath(path)
        return found
    }
}