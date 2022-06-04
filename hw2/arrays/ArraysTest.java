package arrays;

import lists.IntList;
import lists.IntListList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @Brendan Wong
 */

public class ArraysTest {
    @Test
    public void basicCatenateTest() {
        int[] A = {1, 2, 3, 4};
        int[] empty = {};
        int[] empty2 = {};
        int[] B = {5, 6, 7, 8};
        assertArrayEquals(Arrays.catenate(A, B), new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        assertArrayEquals(Arrays.catenate(A, empty), new int[]{1, 2, 3, 4});
        assertArrayEquals(Arrays.catenate(empty, B), new int[]{5, 6, 7, 8});
        assertArrayEquals(Arrays.catenate(empty, empty2), new int[]{});


    }

    @Test
    public void basicRemoveTests() {
        int[] remove = {0, 1, 2, 3};
        assertArrayEquals(Arrays.remove(remove, 1, 2), new int[]{0, 3});
        assertArrayEquals(Arrays.remove(remove, 1, 3), new int[]{0});
        assertArrayEquals(Arrays.remove(remove, 1, 4), null);
        assertArrayEquals(Arrays.remove(remove, 1, 0), new int[]{0, 1, 2, 3});

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
