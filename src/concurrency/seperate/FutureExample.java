package concurrency.seperate;

import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Thread Example
        Callable<String> callable = () -> {
            TimeUnit.SECONDS.sleep(3);
            return "Hi";
        };

        FutureTask<String> f = new FutureTask<>(callable);
        Thread t1 = new Thread(f);
        t1.start();
        // block
        System.out.println(f.get());

        // Thread Pool Example
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        FutureTask<String> f2 = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(4);
            return "hi";
        });

        executorService.submit(f2);
        // block
        System.out.println(f2.get());
    }
}
