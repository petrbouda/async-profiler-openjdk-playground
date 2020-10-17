package pbouda.asyncprofiler.openjdk;

import com.sun.management.HotSpotDiagnosticMXBean;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;

public class HeapDumper {

    private static final String DIAGNOSTIC_BEAN = "com.sun.management:type=HotSpotDiagnostic";

    public static void main(String[] args) throws IOException {
        System.out.println("Heap Dumper Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
                server, DIAGNOSTIC_BEAN, HotSpotDiagnosticMXBean.class);

        mxBean.dumpHeap("/tmp/heap-" + UUID.randomUUID().toString() + ".hprof", true);
    }
}
