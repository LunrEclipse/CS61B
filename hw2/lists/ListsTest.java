package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {

    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertEquals(result, Lists.naturalRuns(input));

        input = IntList.list(1, 2, 3, 4, 5);
        run1 = IntList.list(1, 2, 3, 4, 5);
        result = IntListList.list(run1);
        assertEquals(result, Lists.naturalRuns(input));

        input = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        run1 = IntList.list(1, 3, 7);
        run2 = IntList.list(5);
        IntList run3 = IntList.list(4, 6, 9, 10);
        IntList run4 = IntList.list(10, 11);
        result = IntListList.list(run1, run2, run3, run4);
        assertEquals(result, Lists.naturalRuns(input));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
