package concurrency.merge;

import java.util.concurrent.*;
import java.util.stream.IntStream;

class TaskOneData {
    String data;
}

class TaskTwoData {
    String data;
}


class MainTask {
    private ArrayBlockingQueue<TaskOneData> taskOne = new ArrayBlockingQueue<>(10);
    private ArrayBlockingQueue<TaskTwoData> taskTwo = new ArrayBlockingQueue<>(20);

    public void setDataOne(TaskOneData data) {
        taskOne.add(data);
    }

    public void setDataTwo(TaskTwoData data) {
        taskTwo.add(data);
    }

    public void merge() {
        TaskOneData taskOneData = taskOne.poll();
        TaskTwoData taskTwoData = taskTwo.poll();
        System.out.println("Task one data : " + taskOneData.data);
        System.out.println("Task one data : " + taskTwoData.data);
        try {
            System.out.println("Start main task merge");
            Thread.sleep(4000);
            System.out.println("Finish main task merge");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class CyclicBarrierExample {

    Executor executor = Executors.newFixedThreadPool(2);
    Executor taskOneExecutor = Executors.newFixedThreadPool(2);
    Executor taskTwoExecutor = Executors.newFixedThreadPool(2);

    MainTask mainTask = new MainTask();
    final CyclicBarrier barrier = new CyclicBarrier(2, () -> {
        executor.execute(() -> mainTask.merge());
    });

    public static void main(String[] args) {
        CyclicBarrierExample example = new CyclicBarrierExample();


        IntStream.range(0, 10).forEach(x -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(x + " time work");
            example.taskOneExecutor.execute(() -> {
                try {
                    Thread.sleep(2000);
                    TaskOneData taskOneData = new TaskOneData();
                    taskOneData.data = "Finish Task One";
                    example.mainTask.setDataOne(taskOneData);
                    example.barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

            example.taskTwoExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    TaskTwoData taskTwoData = new TaskTwoData();
                    taskTwoData.data = "Finish Task One";
                    example.mainTask.setDataTwo(taskTwoData);
                    example.barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        });
    }

}
