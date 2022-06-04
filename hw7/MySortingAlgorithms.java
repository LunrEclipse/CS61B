import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                int cur = i;
                while (cur > 0 && array[cur-1] > array[cur]) {
                    swap(array, cur-1, cur);
                    cur--;
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for(int i = 0; i < k - 1; i++) {
                int min = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < array[min]) {
                        min = j;
                    }
                }
                swap(array, min, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            mergeSort(array, 0, k - 1);

        }

        public void mergeSort(int[] array, int start, int end) {
            if (start < end) {
                int half = (start + end) / 2;
                mergeSort(array, start, half);
                mergeSort(array ,half + 1, end);
                merge(array, start, half, end);
            }
        }

        private void merge(int[] array, int start, int mid, int end) {
            int[] arr1 = new int[mid - start + 1];
            int[] arr2 = new int[end - mid];

            System.arraycopy(array, start, arr1, 0, arr1.length);
            System.arraycopy(array, mid + 1, arr2, 0, arr2.length);

            int count1 = 0;
            int count2 = 0;

            for(int i = start; i <= end; i++) {
                if (count1 >= arr1.length) {
                    array[i] = arr2[count2];
                    count2++;
                } else if (count2 >= arr2.length) {
                    array[i] = arr1[count1];
                    count1++;
                } else if (arr1[count1] < arr2[count2]) {
                    array[i] = arr1[count1];
                    count1++;
                } else {
                    array[i] = arr2[count2];
                    count2++;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int max = a[0];
            for (int i = 0; i < k; i++) {
                if (a[i] > max) {
                    max = a[i];
                }
            }

            int places = 0;
            while (max > 0) {
                places++;
                max /= 10;
            }

            int placeValue = 1;
            while (places > 0) {
                radix(a, k, placeValue);
                placeValue *= 10;
                places--;
            }
        }

        public void radix(int[] a, int k, int place) {
            int max = 10;
            int[] frequency = new int[max];

            for (int i = 0; i < k; i++) {
                frequency[a[i] / place % 10]++;
            }

            for (int i = 1; i < max; i++) {
                frequency[i] += frequency[i - 1];
            }

            int[] res = new int[k];

            for (int i = k - 1; i >= 0; i--) {
                int digit = (a[i] / place) % max;
                res[frequency[digit] - 1] = a[i];
                frequency[digit]--;
            }

            System.arraycopy(res, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
