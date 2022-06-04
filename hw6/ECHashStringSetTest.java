import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {

    @Test
    public void basicAdd() {
        ECHashStringSet map = new ECHashStringSet();
        map.put("f");
        for (int i = 0; i <= 10; i++) {
            if (i != 5) {
                map.put("" + (char)('a' + i));
            }
        }
        for (int i = 0; i <= 10; i++) {
            assertTrue(map.contains("" + (char)('a' + i)));
        }
        System.out.println(map.asList());
    }

    @Test
    public void addOutOfOrder() {
        ECHashStringSet map = new ECHashStringSet();
        map.put("g");
        map.put("b");
        map.put("a");
        map.put("f");
        map.put("h");
        map.put("c");
        map.put("e");
        map.put("i");
        map.put("d");
        map.put("j");
        map.put("k");
        for (int i = 0; i <= 10; i++) {
            assertTrue(map.contains("" + (char)('a' + i)));
        }
        System.out.println(map.asList());
    }

    @Test
    public void largerTree() {
        ECHashStringSet map = new ECHashStringSet();
        map.put("m");
        for (int i = 0; i <= 25; i++) {
            if (i != 12) {
                map.put("" + (char)('a' + i));
            }
        }
        for (int i = 0; i <= 25; i++) {
            assertTrue(map.contains("" + (char)('a' + i)));
        }
        System.out.println(map.asList());
    }
}
