public class Main {

    public static void main (String[] args) {

        byte x = 82;    // 01010010
        byte y = -54;   // 11001010


        byte temp = (byte) (~ x);
        System.out.println(temp);
    }
}
