package concurrency.merge;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class TaskSet {
    public void taskOne() {
        try {
            String currentThread = Thread.currentThread().getName();
            System.out.println("T1 task start in " + currentThread);
            // do something that need long time
            Thread.sleep(4000);
            System.out.println("T1 task finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void taskTwo() {
        {
            try {
                String currentThread = Thread.currentThread().getName();
                System.out.println("T2 task start in " + currentThread);
                // do something that need long time
                Thread.sleep(2000);
                System.out.println("T2 task finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class CountDownLatchExample {

    public Executor executor = Executors.newFixedThreadPool(2);

    private CountDownLatch countDownLatch;

    public CountDownLatchExample(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void fork(Runnable task) {
        executor.execute(() -> {
            task.run();
            countDownLatch.countDown();
        });

    }

    public void merge() throws InterruptedException {
        countDownLatch.await();
        System.out.println("T1 task and T2 task are all already");
        System.out.println("Do the main thread task");
    }

    public void solve(TaskSet taskSet) throws InterruptedException {
        System.out.println("Start t1 and t2 task");
        fork(taskSet::taskOne);
        fork(taskSet::taskTwo);
        merge();
    }


    public static void main(String[] args) throws InterruptedException {
        while (true) {
            CountDownLatchExample countDownLatchExample = new CountDownLatchExample(new CountDownLatch(2));
            countDownLatchExample.solve(new TaskSet());
        }
    }
}
