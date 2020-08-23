package concurrency.executor;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class ThreadFactoryExample implements ThreadFactory {

    // 創建Thread Factory統一此Thread Pool的Thread都在同個Group

    ThreadGroup tg = new ThreadGroup("Example Group");

    @Override
    public Thread newThread(Runnable r) {
        System.out.println("Create Thread");
        return new Thread(tg, r, "Example Thread");
    }
}

public class ThreadPoolExecutorExample {
    public static void main(String[] args) {
        // 設定雙核心的Thread Pool，當資源非常吃緊時Thread會增加到10個
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2,
                10,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadFactoryExample(),
                new ThreadPoolExecutor.DiscardPolicy());

        // 增加100個任務
        IntStream.range(0, 100).forEach(x -> {
            Runnable r = () -> {
                System.out.println("Hi I am task " + x);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            try {
                // 模擬任務提交頻率 用來測試RejectedHandler
                int sleepTime;
                if (x < 20)
                    sleepTime = new Random().nextInt(1000);
                else if (x < 40)
                    sleepTime = new Random().nextInt(500);
                else if (x < 80)
                    sleepTime = new Random().nextInt(300);
                else
                    sleepTime = 0;

                System.out.println("Sleep " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tpe.execute(r);
        });
    }
}
