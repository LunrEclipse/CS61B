import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {

    @Test
    public void basicAdd() {
        BSTStringSet tree = new BSTStringSet();
        ArrayList<String> correct = new ArrayList<>();
        tree.put("f");
        for (int i = 0; i <= 10; i++) {
            if (i != 5) {
                tree.put("" + (char)('a' + i));
            }
        }
        for (int i = 0; i <= 10; i++) {
            assertTrue(tree.contains("" + (char)('a' + i)));
            correct.add("" + (char)('a' + i));
        }
        assertEquals(tree.asList(), correct);
    }

    @Test
    public void addOutOfOrder() {
        BSTStringSet tree = new BSTStringSet();
        ArrayList<String> correct = new ArrayList<>();
        tree.put("g");
        tree.put("b");
        tree.put("a");
        tree.put("f");
        tree.put("h");
        tree.put("c");
        tree.put("e");
        tree.put("i");
        tree.put("d");
        tree.put("j");
        tree.put("k");
        for (int i = 0; i <= 10; i++) {
            assertTrue(tree.contains("" + (char)('a' + i)));
            correct.add("" + (char)('a' + i));
        }
        assertEquals(tree.asList(), correct);
    }

    @Test
    public void largerTree() {
        BSTStringSet tree = new BSTStringSet();
        ArrayList<String> correct = new ArrayList<>();
        tree.put("m");
        for (int i = 0; i <= 25; i++) {
            if (i != 12) {
                tree.put("" + (char)('a' + i));
            }
        }
        for (int i = 0; i <= 25; i++) {
            assertTrue(tree.contains("" + (char)('a' + i)));
            correct.add("" + (char)('a' + i));
        }
        assertEquals(tree.asList(), correct);
    }
}
