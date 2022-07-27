package graphs.shortestpaths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.List;
import java.util.Map;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) { // Dijkstra's Implementation
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> edgeTo = new HashMap<>();
        ExtrinsicMinPQ<V> edgeOrder = createMinPQ();

        distTo.put(start, 0.0); // Our first known node
        edgeOrder.add(start, 0.0); // Explores start node first

        while (!edgeOrder.isEmpty()) {
            V node = edgeOrder.removeMin(); // Find the closest node
            if (java.util.Objects.equals(node, end)) {
                return edgeTo;
            }

            for (E edge : graph.outgoingEdgesFrom(node)) { // For every edge coming from our node
                V temp = edge.to();
                if (!distTo.containsKey(temp)) { // If we haven't visited it yet
                    distTo.put(temp, Double.POSITIVE_INFINITY); // Set the distance to that node = infinity
                } // Prevents nullPointerExceptions when comparing distTo values

                double old = distTo.get(temp); // Otherwise, get the old distance to that node
                double curr = distTo.get(edge.from()) + edge.weight(); // Get the new distance too
                if (curr < old) { // Compare them
                    distTo.put(temp, curr); // If the new distance is shorter, save that distance to the node
                    edgeTo.put(temp, edge); // Save the reference to the edge as well
                    if (edgeOrder.contains(temp)) {
                        edgeOrder.changePriority(temp, curr); // Shortens the path in edgeOrder for future
                    } else {
                        edgeOrder.add(temp, curr); // Adds perimeter nodes to the list to be explored
                    }
                }
            }
        }
        return edgeTo; // Return the map of our relationships thus far
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        List<E> edges = new ArrayList<>();
        Stack<E> reverseEdges = new Stack<>();
        if (java.util.Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        V temp = end;
        while (!java.util.Objects.equals(temp, start)) { // Repeat until we're looking at the start
            if (!spt.containsKey(temp)) {
                return new ShortestPath.Failure<>();
            }
            reverseEdges.push(spt.get(temp));
            temp = spt.get(temp).from(); // Change the current node to being the predecessor
        }
        while (!reverseEdges.isEmpty()) {
            edges.add(reverseEdges.pop());
        }
        return new ShortestPath.Success<>(edges);
    }

}
