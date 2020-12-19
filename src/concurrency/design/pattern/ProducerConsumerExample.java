package concurrency.design.pattern;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class Producer {

    private BlockingQueue<Runnable> eventPool;
    private Executor workerPool = Executors.newFixedThreadPool(4);

    Producer(BlockingQueue<Runnable> pool) {
        eventPool = pool;
    }

    public void start() {
        for (int i = 1; i < 4; i++) {
            int finalI = i;
            workerPool.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(finalI * 2000);
                        System.out.println("Producer " + finalI + "put a task in q");
                        eventPool.put(() -> {
                            try {
                                System.out.println("Start running task. It need 2 sec");
                                Thread.sleep(5000);
                                System.out.println("Finish task");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

class Consumer {

    private BlockingQueue<Runnable> eventPool;
    private Executor workerPool = Executors.newFixedThreadPool(2);

    Consumer(BlockingQueue<Runnable> pool) {
        eventPool = pool;
    }

    public void start() {
        for (int i = 1; i < 3; i++) {
            int finalI = i;
            workerPool.execute(() -> {
                while (true) {
                    try {
                        Runnable task = eventPool.take();
                        System.out.println("Consumer " + finalI + " start a task");
                        task.run();
                        System.out.println("Consumer " + finalI + " finish a task");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

public class ProducerConsumerExample {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> q = new LinkedBlockingQueue<>();
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);
        p.start();
        c.start();
    }
}
