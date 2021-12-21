package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> n = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opcounts = new AList<Integer>();
        SLList<Integer> Slist = new SLList<Integer>();
        for(int i=1000;i<=128000;i=i*2){
            n.addLast(i);
            opcounts.addLast(10000);
            for(int j=0;j<i;j++){
                Slist.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for(int m=0;m<10000;m++)
                Slist.getLast();
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        printTimingTable(n,times,opcounts );
    }

}
