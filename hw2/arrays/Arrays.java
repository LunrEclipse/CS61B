package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @Brendan Wong
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] combine = new int[A.length + B.length];
        int index = 0;
        for(int i = 0; i < A.length; i++) {
            combine[index] = A[i];
            index++;
        }
        for(int i = 0; i < B.length; i++) {
            combine[index] = B[i];
            index++;
        }
        return combine;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        if(start + len > A.length) {
            return null;
        }
        else {
            int[] remove = new int[A.length - len];
            int index = 0;
            for(int i = 0; i  < start; i++) {
                remove[index] = A[i];
                index++;
            }
            for(int i = start + len; i  < A.length; i++) {
                remove[index] = A[i];
                index++;
            }
            return remove;
        }
    }

}
