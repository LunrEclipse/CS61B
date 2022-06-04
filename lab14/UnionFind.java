
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        parents = new int[N+1];
        sizes = new int[N+1];
        for (int i = 1; i <= N; i++) {
            parents[i] = i;
            sizes[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (parents[v] == v) {
            return v;
        } else {
            return find(parents[v]);
        }
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int root1 = find(u);
        int root2 = find(v);
        if (root1 == root2) {
            return root1;
        } else {
            int size1 = sizes[root1];
            int size2 = sizes[root2];
            if (size1 > size2) {
                parents[root2] = root1;
                sizes[root1] += size2;
                return root1;
            } else {
                parents[root1] = root2;
                sizes[root2] += size1;
                return root2;
            }
        }
    }

    public int[] parents;
    public int[] sizes;
}
