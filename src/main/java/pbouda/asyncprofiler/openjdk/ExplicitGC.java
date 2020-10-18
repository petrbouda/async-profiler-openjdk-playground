package pbouda.asyncprofiler.openjdk;

import java.io.IOException;

public class ExplicitGC {

    public static void main(String[] args) throws IOException {
        System.out.println("Explicit GC Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        System.gc();
    }
}
