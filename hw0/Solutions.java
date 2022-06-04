/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and Brendan Wong
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        // TODO: Your code here. Replace the following return statement.
        if(x % 2 == 0) {
            return True;
        } else {
            return False;
        }
    }

    public static int max(int[] a) {
        int max = 0;
        for(int i = 0; i < a.length; i++) {
            if(a[i] >= max) {
                max = a[i];
            }
        }
        return max;
    }

    public static boolean wordBank(String word, String[] bank) {
        for(int i = 0; i < bank.length; i++) {
            if(bank[i].equals(word))
                return True;
        }
        return False;
    }

    public static boolean threeSum(int[] a) {
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a.length; j++) {
                for(int k = 0; k < a.length; k++) {
                    if(a[i] + a[j] + a[k] == 0)
                        return True;
                }
            }
        }
        return False;
    }


    // TODO: Fill in the method signatures for the other exercises
    // Your methods should be static for this HW. DO NOT worry about what this means.
    // Note that "static" is not necessarily a default, it just happens to be what
    // we want for THIS homework. In the future, do not assume all methods should be
    // static.

}
