package flik;

import static org.junit.Assert.assertTrue;

/** An Integer tester created by Flik Enterprises.
 * @author Josh Hug
 * */
public class Flik {
    /** @param a Value 1
     *  @param b Value 2
     *  @return Whether a and b are the same */
    public static boolean isSameNumber(Integer a, Integer b) {

        /*if (a == 128) {
            System.out.println("a is " + a + " b is" + b);
            if (b == a) {
                System.out.println("a == b ");
            }
            assertTrue("equal",a == 128);
        }
        if (b == 128) {
            System.out.println("a is " + a + " b is" + b);
            if (b.equals(a)) {
                System.out.println("a == b ");
            }
            assertTrue("equal",b == 128);
        }*/
        //assertTrue("equal",a == b);
       // return a == b;
        return a.equals(b);
    }
}
