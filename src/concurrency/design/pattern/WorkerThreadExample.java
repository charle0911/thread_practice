package concurrency.design.pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;


class ThreadPool {
    List<Thread> threadPool;
    BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    // 建構者限定Thread數量
    ThreadPool(int threadCnt) {
        threadPool = new ArrayList<>(threadCnt);
        IntStream.range(0, threadCnt).forEach(x -> {
            threadPool.add(new Thread(() -> {
                try {
                    // 每個執行緒會不斷的執行下方邏輯並且處理
                    while (true) {
                        System.out.println("Thread -" + x + " start work");
                        System.out.println("Thread -" + x + " take job");
                        // LinkedBlockingQueue的take當沒有物件時會BLOK住，讓程式停在這，減少cpu浪費
                        Runnable r = tasks.take();
                        System.out.println("Thread -" + x + " got job");
                        r.run();
                        System.out.println("Thread -" + x + " wait next job");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Worker-" + threadCnt));
            threadPool.get(x).start();
        });

    }

    // 提交方法
    public void submit(Runnable runnable) {
        tasks.add(runnable);
    }

    public void close() {
        threadPool.forEach(Thread::interrupt);
    }
}

public class WorkerThreadExample {
    public static void main(String[] args) throws IOException {
        ThreadPool pool = new ThreadPool(2);
        Scanner s = new Scanner(System.in);
        while (true) {
            int i = s.nextInt();
            // 輸入0的時候提交任務
            if (i == 0) {
                System.out.println("Submit a task");
                pool.submit(() -> {
                    System.out.println("Add task");
                    // fake process data
                    try {
                        // 任務sleep 2秒模擬處理邏輯
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else if (i == 1) {
                System.out.println("Close the pool");
                pool.close();
                break;
            }
        }
    }
}
