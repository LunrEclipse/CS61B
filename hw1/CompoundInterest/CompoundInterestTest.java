import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2022));
        assertEquals(1, CompoundInterest.numYears(2023));
        assertEquals(100, CompoundInterest.numYears(2122));
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).

        // The variable below can be used when you write your tests.
        double tolerance = 0.02;
        assertEquals(1, CompoundInterest.futureValue(1, 0, 2022), tolerance);
        assertEquals(0, CompoundInterest.futureValue(0, 100, 2022), tolerance);
        assertEquals(2.3, CompoundInterest.futureValue(2, 15, 2023), tolerance);
        assertEquals(2.645, CompoundInterest.futureValue(2, 15, 2024), tolerance);
        assertEquals(2199761.25634, CompoundInterest.futureValue(1000, 8, 2122), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.02;
        assertEquals(1, CompoundInterest.futureValueReal(1, 0, 2022, 0), tolerance);
        assertEquals(0, CompoundInterest.futureValueReal(0, 100, 2022, 100), tolerance);
        assertEquals(2.369, CompoundInterest.futureValueReal(2, 15, 2023, -3), tolerance);
        assertEquals(2.4886805, CompoundInterest.futureValueReal(2, 15, 2024, 3), tolerance);
        assertEquals(805183.7629, CompoundInterest.futureValueReal(1000, 8, 2122, 1), tolerance);
        assertEquals(295712.29, CompoundInterest.futureValueReal(1000000, 0, 2062, 3), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.02;
        assertEquals(1, CompoundInterest.totalSavings(1, 2022, 10), tolerance);
        assertEquals(0, CompoundInterest.totalSavings(0, 2062, 10), tolerance);
        assertEquals(4878518.11, CompoundInterest.totalSavings(10000, 2062, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.02;
        assertEquals(1, CompoundInterest.totalSavingsReal(1, 2022, 10, 10), tolerance);
        assertEquals(0, CompoundInterest.totalSavingsReal(0, 2062, 10, 10), tolerance);
        assertEquals(1442637.75, CompoundInterest.totalSavingsReal(10000, 2062, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
