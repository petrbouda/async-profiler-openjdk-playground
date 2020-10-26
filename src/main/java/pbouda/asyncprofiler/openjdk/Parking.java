package pbouda.asyncprofiler.openjdk;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Parking {

    private static final Object BLOCKER = new Object();

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws Exception {
        System.out.println("LockSupport Parking Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        while (true) {
            // parkOneSecond();
            parkAndUnpark();
        }
    }

    // pthread_cond_timedwait
    private static void parkOneSecond() {
        System.out.println("Starts sleeping");
        LockSupport.parkNanos(Duration.ofSeconds(1).toNanos());
        System.out.println("Waked up");
    }

    // pthread_cond_wait
    // pthread_cond_signal
    private static void parkAndUnpark() {
        Thread parkedThread = Thread.currentThread();
        SCHEDULER.schedule(() -> LockSupport.unpark(parkedThread), 1, TimeUnit.SECONDS);
        System.out.println("Starts sleeping");
        LockSupport.park();
        System.out.println("Waked up");
    }

    /**
     * BLOCKER is used as a synchronization point
     * "volatile barrier" to synchronize the memory.
     */
    private static void parkOneSecondWithBlocker() {
        LockSupport.parkNanos(BLOCKER, Duration.ofSeconds(1).toNanos());
    }
}
