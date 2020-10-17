package pbouda.asyncprofiler.openjdk;

import java.io.IOException;

public class HumongousAllocator {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Humongous Allocator Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        while (true) {
            byte[] bytes = new byte[10 * 1024 * 1024];
            System.out.println("Allocated: " + bytes.length);
            Thread.sleep(50);
        }
    }
}
