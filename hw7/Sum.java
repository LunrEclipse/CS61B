import java.sql.Array;
import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        int[] combined = new int[A.length + B.length];
        System.arraycopy(A, 0, combined, 0, A.length);
        System.arraycopy(B, 0,  combined, A.length, B.length);
        Arrays.sort(combined);

        int start = 0;
        int end = combined.length - 1;

        while (start < end) {
            int cur = combined[start] + combined[end];
            if (cur == m) {
                return true;
            } else if (cur < m) {
                start++;
            } else {
                end--;
            }
        }
        return false;

    }
}
