import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Brendan Wong
 */
class ECHashStringSet implements StringSet {

    private static class Node {
        private String s;
        private Node next;
        Node(String sp) {
            s = sp;
        }
    }

    public ECHashStringSet() {
        list =  new Node[size];
    }

    private int numElements = 0;
    private int size = 10;
    private Node[] list;

    @Override
    public void put(String s) {
       int key = getKey(s);
       Node cur = list[key];
       if (cur == null) {
           list[key] = new Node(s);
       } else {
           while (cur.next != null) {
               cur = cur.next;
           }
           cur.next = new Node(s);
       }
       numElements++;
       if (numElements / size >= 5) {
           reconstruct();
       }
    }

    @Override
    public boolean contains(String s) {
        int key = getKey(s);
        Node cur = list[key];
        while (cur != null) {
            if (cur.s.equals(s)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            Node cur = list[i];
            while (cur != null) {
                values.add(cur.s);
                cur = cur.next;
            }
        }
        return values;
    }

    private int getKey(String s) {
        return (s.hashCode() & 0x7fffffff) % size;
    }

    private void reconstruct() {
        ArrayList<String> elems = (ArrayList<String>) asList();
        size = size * 2;
        list = new Node[size];
        for (int i = 0; i < elems.size(); i++) {
            put(elems.get(i));
        }
    }
}
