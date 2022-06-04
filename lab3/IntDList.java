/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int count = 0;
        DNode cur = _front;
        while(cur != null) {
            count++;
            cur = cur._next;
        }
        return count;
    }

    /**
     * @param index index of node to return,
     *          where index = 0 returns the first node,
     *          index = 1 returns the second node, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The node at index index
     */
    private DNode getNode(int index) {
        DNode cur = _front;
        for(int i = 0; i < index; i++) {
            cur = cur._next;
        }
        return cur;
    }

    /**
     * @param index index of element to return,
     *          where index = 0 returns the first element,
     *          index = 1 returns the second element,and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The integer value at index index
     */
    public int get(int index) {
        return getNode(index)._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if(_front == null) {
            _front = _back = new DNode(null, d, null);
        } else {
            DNode newFront = new DNode(null, d, _front);
            _front._prev = newFront;
            _front = newFront;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if(_back == null) {
            _front = _back = new DNode(null, d, null);
        } else {
            DNode newBack = new DNode(_back, d, null);
            _back._next = newBack;
            _back = newBack;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position, and so onh.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size.
     */
    public void insertAtIndex(int d, int index) {
        if(index == 0) {
            insertFront(d);
        } else if(index == size()) {
            insertBack(d);
        } else {
            DNode cur = _front;
            for(int i = 0; i < index; i++) {
                cur = cur._next;
            }
            DNode previous = cur._prev;
            DNode newIndex = new DNode(previous, d, cur);
            previous._next = newIndex;
            cur._prev = newIndex;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     * Assume `deleteFront` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        if(size() == 1) {
            int val = _front._val;
            _front = _back = null;
            return val;
        } else {
            DNode oldFront = _front;
            _front = _front._next;
            _front._prev = null;
            return oldFront._val;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     * Assume `deleteBack` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        if(size() == 1) {
            int val = _back._val;
            _front = _back = null;
            return val;
        } else {
            DNode oldBack = _back;
            _back = _back._prev;
            _back._next = null;
            return oldBack._val;
        }
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        if(index == 0) {
            int val = _front._val;
            deleteFront();
            return val;
        } else if(index == size()-1) {
            int val = _back._val;
            deleteBack();
            return val;
        } else {
            DNode cur = _front;
            for(int i = 0; i < index; i++) {
                cur = cur._next;
            }
            DNode previous = cur._prev;
            DNode next = cur._next;
            previous._next = next;
            next._prev = previous;
            return cur._val;
        }
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
