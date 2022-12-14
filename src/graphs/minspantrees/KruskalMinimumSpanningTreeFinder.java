package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
    
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        int ticker = 0;

        DisjointSets<V> disjointSets = createDisjointSets();
        Set<E> finalMST = new HashSet<>();

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        for (E edge : edges) {
            int uMST = disjointSets.findSet(edge.from());
            int vMST = disjointSets.findSet(edge.to());

            if (uMST != vMST) {
                finalMST.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }

        for (E edge : finalMST) {
            ticker++;
        }

        if (!(ticker == graph.allVertices().size() - 1) && graph.allVertices().size() > 0) {
            return new MinimumSpanningTree.Failure<>();
        }

        return new MinimumSpanningTree.Success<>(finalMST);
    }
}
