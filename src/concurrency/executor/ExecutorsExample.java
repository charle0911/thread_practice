package concurrency.executor;

import java.util.concurrent.Executors;

public class ExecutorsExample {
    public static void main(String[] args) {
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool()
    }
}
