package randomizedtest;

import afu.org.checkerframework.checker.igj.qual.I;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> noResizing =new AListNoResizing<>();
        BuggyAList<Integer> buggyAList =new BuggyAList<>();
        for(int i=4;i<7;i++) {
            noResizing.addLast(i);
            buggyAList.addLast(i);
        }
        assertEquals(noResizing.size(),buggyAList.size());
        assertEquals(noResizing.removeLast(),buggyAList.removeLast());
        assertEquals(noResizing.removeLast(),buggyAList.removeLast());
        assertEquals(noResizing.removeLast(),buggyAList.removeLast());

    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B=new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("L.size: " + size);
                assertEquals(L.size(),B.size());
            }else if (L.size()==0) {
                continue;
            }else if(operationNumber==2){
                System.out.println("L.getLast(" +L.getLast() + ")");
                System.out.println("B.getLast(" +B.getLast() + ")");
                assertEquals(L.getLast(),B.getLast());
            }else if(operationNumber==3){
                //System.out.println("L.removeLast(" +L.removeLast() + ")");
                //System.out.println("B.removeLast(" +B.removeLast() + ")");
                assertEquals(L.removeLast(),B.removeLast());
            }
        }
    }
}
