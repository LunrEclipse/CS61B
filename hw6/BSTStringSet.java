import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (_root == null) {
            _root = new Node(s);
        } else {
            Node cur = _root;
            while (true) {
                if (s.compareTo(cur.s) < 0 ) {
                    if (cur.left == null) {
                        cur.left = new Node(s);
                        break;
                    } else {
                        cur = cur.left;
                    }
                } else if (s.compareTo(cur.s) > 0) {
                    if (cur.right == null) {
                        cur.right = new Node(s);
                        break;
                    } else {
                        cur = cur.right;
                    }

                } else {
                    cur.s = s;
                    break;
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        if (_root == null) {
            return false;
        } else {
            Node cur = _root;
            while (true) {
                if (cur == null) {
                    return false;
                }
                else if (s.compareTo(cur.s) < 0 ) {
                    cur = cur.left;
                } else if (s.compareTo(cur.s) > 0) {
                    cur = cur.right;
                } else {
                    return  true;
                }
            }
        }
    }

    @Override
    public List<String> asList() {
        if (_root == null) {
            return new ArrayList<>();
        } else {
            return listHelper(_root);
        }
    }

    private List<String> listHelper(Node n) {
        ArrayList<String> list = new ArrayList<>();
        if (n.left != null) {
            list.addAll(listHelper(n.left));
        }
        list.add(n.s);
        if (n.right != null) {
            list.addAll(listHelper(n.right));
        }
        return list;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B (OPTIONAL)
    }


    /** Root node of the tree. */
    private Node _root;
}
