package pbouda.asyncprofiler.openjdk;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

public class UnsafeAllocator {

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException, IllegalAccessException, IOException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        System.out.println("PAGE SIZE: " + unsafe.pageSize());
        System.out.println("Unsafe Allocator Started: PID: " + ProcessHandle.current().pid());
        System.in.read();

        while (true) {
            long address = unsafe.allocateMemory(1024 * 1024);
            System.out.println("ALLOCATED " + address);
            Thread.sleep(50);
        }
    }
}
