package disjointsets;

import java.util.ArrayList;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private List<T> items;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        items = new ArrayList<>();
    }

    @Override
    public void makeSet(T item) {
        pointers.add(-1);
        items.add(item);
    }

    @Override
    public int findSet(T item) {
        for (int i = 0; i < items.size(); i++) {
            if (java.util.Objects.equals(items.get(i), item)) {
                return findSetHelper(i);
            }
        }
        throw new IllegalArgumentException();
    }

    private int findSetHelper(int index) {
        if (pointers.get(index) < 0) {
            return index;
        }
        pointers.set(index, findSetHelper(pointers.get(index)));
        return pointers.get(index);
    }


    @Override
    public boolean union(T item1, T item2) {
        int item1ID = findSet(item1);
        int item2ID = findSet(item2);

        if (item1ID != item2ID) {
            int item1Weight = pointers.get(item1ID);
            int item2Weight = pointers.get(item2ID);

            if (item1Weight < item2Weight) {
                pointers.set(item1ID, item1Weight + item2Weight);
                pointers.set(item2ID, item1ID);
            } else {
                pointers.set(item2ID, item1Weight + item2Weight);
                pointers.set(item1ID, item2ID);
            }
            return true;
        }
        return false;
    }

}
