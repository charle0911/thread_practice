package concurrency.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ThreadPool {

    // 籃子
    BlockingQueue<Runnable> workQueue;

    // 消費者
    List<WorkerThread> threads = new ArrayList<>();

    ThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for (int idx = 0; idx < poolSize; idx++) {
            WorkerThread work = new WorkerThread();

            // 消費者源源不絕拿便當
            work.start();
            threads.add(work);
        }
    }

    void execute(Runnable command) throws InterruptedException {
        // 老闆將便當放到籃子
        workQueue.put(command);
    }

    class WorkerThread extends Thread {
        public void run() {
            while (true) {
                Runnable task = null;
                try {
                    // 有便當就拿 / 沒有就block住等待
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        }
    }
}

public class ThreadPoolExample {


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        ThreadPool pool = new ThreadPool(10, workQueue);
        pool.execute(() -> {
            System.out.println("hello");
        });

    }
}
