package concurrency.tool;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class SemaphoreExample {
    Semaphore semaphore = new Semaphore(1);

    private int cnt = 0;

    public void threadSafeAction() {
        try {
            semaphore.acquire();
            try {
                cnt += 1;
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreExample se = new SemaphoreExample();
        IntStream.range(0, 100).forEach(
                x -> {
                    Thread t = new Thread(se::threadSafeAction, "Thread " + x + " is running");
                    System.out.println(t.getName());
                    t.start();
                }
        );

        Thread.sleep(1000);
        System.out.println(se.cnt);
    }

}
