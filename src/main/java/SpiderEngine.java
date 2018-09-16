import java.util.*;
import java.util.concurrent.TimeUnit;

public class SpiderEngine {

    public final static HashSet<String> visited = new HashSet<String>();
    public final static Queue<String> toVisit = new LinkedList<String>();
    private static final int THREAD_NUM  = 10;


    public SpiderEngine() {

    }

    public void run(List<String> seeds) {
        SpiderLeg leg = new SpiderLeg("China");
        toVisit.addAll(seeds);
        List<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < THREAD_NUM; i ++){
            Thread t = new Thread(leg, Integer.toString(i));
            threadList.add(t);
            t.start();
        }
        while (true) {
            for (Thread t : threadList) {
                if (!t.isAlive()) {
                    Thread newThread = new Thread(leg, t.getName());
                    threadList.remove(t);
                    threadList.add(newThread);
                    newThread.start();
                    break;
                }
            }
        }
    }
}
