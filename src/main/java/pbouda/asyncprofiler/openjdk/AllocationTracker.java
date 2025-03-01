package pbouda.asyncprofiler.openjdk;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllocationTracker {

    private static final String LONG_TEXT = """
            In this mode profiler collects stack trace samples that include Java methods, 
            native calls, JVM code and kernel functions. The general approach is receiving 
            call stacks generated by perf_events and matching them up with call stacks 
            generated by AsyncGetCallTrace, in order to produce an accurate profile of both 
            Java and native code. Additionally, async-profiler provides a workaround to recover 
            stack traces in some corner cases where AsyncGetCallTrace fails.

            This approach has the following advantages compared to using perf_events directly 
            with a Java agent that translates addresses to Java method names: Works on older 
            Java versions because it doesn't require -XX:+PreserveFramePointer, which is only 
            available in JDK 8u60 and later. Does not introduce the performance overhead from 
            -XX:+PreserveFramePointer, which can in rare cases be as high as 10%.
            Does not require generating a map file to map Java code addresses to method names.
            Works with interpreter frames.

            Does not require writing out a perf.data file for further processing in user space scripts.
            If you wish to resolve frames within libjvm, the debug symbols are required.
            """;


    // -Xmx256m -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
    // -Xmx256m -XX:+UseSerialGC
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Memory Allocator Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        // we need to stop producing the garbage before the JVM blows up
        // otherwise we won't be able to dump data for AsyncProfiler
        //
        // CTRL-C before it reaches the end, otherwise, we see DestroyVM thread instead of Main

        ExecutorService executor = Executors.newFixedThreadPool(3);
        Runnable allocation = () -> {
            for (int i = 0; true; i++) {
                String allocated = LONG_TEXT.repeat(i);
                System.out.println("ALLOCATED " + allocated.length());
                heapMemory();
            }
        };
        
        executor.submit(allocation);
        executor.submit(allocation);
        executor.submit(allocation);
    }

    public static long heapMemory() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage HEAP_MEMORY = memoryMXBean.getHeapMemoryUsage();

        System.out.println("INIT/USED/COMMITTED/MAX : "
                           + toMB(HEAP_MEMORY.getInit()) + "/"
                           + toMB(HEAP_MEMORY.getUsed()) + "/"
                           + toMB(HEAP_MEMORY.getCommitted()) + "/"
                           + toMB(HEAP_MEMORY.getMax()));

        return toMB(HEAP_MEMORY.getUsed());
    }

    private static long toMB(long bytes) {
        return (bytes >> 20);
    }
}
