import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = new int[3][3];
        assertEquals(0, MultiArr.maxValue(arr));
        arr[2][2] = 9;
        assertEquals(9, MultiArr.maxValue(arr));
        arr[2][1] = 8;
        assertEquals(9, MultiArr.maxValue(arr));
        arr[2][0] = 7;
        arr[1][2] = 6;
        arr[1][1] = 5;
        arr[1][0] = 4;
        arr[0][2] = 3;
        arr[0][1] = 2;
        arr[0][0] = 1;
        assertEquals(9, MultiArr.maxValue(arr));
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = new int[3][3];
        assertEquals(0, MultiArr.allRowSums(arr)[0]);
        assertEquals(0, MultiArr.allRowSums(arr)[1]);
        assertEquals(0, MultiArr.allRowSums(arr)[2]);
        arr[2][2] = 9;
        assertEquals(0, MultiArr.allRowSums(arr)[0]);
        assertEquals(0, MultiArr.allRowSums(arr)[1]);
        assertEquals(9, MultiArr.allRowSums(arr)[2]);
        arr[2][1] = 8;
        arr[2][0] = 7;
        arr[1][2] = 6;
        arr[1][1] = 5;
        arr[1][0] = 4;
        arr[0][2] = 3;
        arr[0][1] = 2;
        arr[0][0] = 1;
        assertEquals(6, MultiArr.allRowSums(arr)[0]);
        assertEquals(15, MultiArr.allRowSums(arr)[1]);
        assertEquals(24, MultiArr.allRowSums(arr)[2]);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
