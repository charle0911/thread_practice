package concurrency.tool;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

class DoSomethingObject {

    // 需要大運算的操作，耗時久佔據多資源 (例如DB連線)
    public void doSomething() {
        System.out.println("I'm doing some action which need a lot of time");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ObjectPool {
    List<DoSomethingObject> list;
    private final Semaphore semaphore;

    // 創建可以執行此運算的 執行池
    ObjectPool(int size, DoSomethingObject t) {
        // 使用Vector確保執行安全 (也可以不使用)
        list = new Vector<>(size);
        IntStream.range(0, size).forEach(x -> {
            list.add(t);
        });

        semaphore = new Semaphore(size);
    }

    public void exec() {
        try {
            DoSomethingObject t = null;
            semaphore.acquire();
            try {
                t = list.remove(0);
                t.doSomething();
            } finally {
                list.add(t);
                semaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

public class SemaphoreObjectPoolExample {

    public static void main(String[] args) throws InterruptedException {
        ObjectPool objectPool = new ObjectPool(2, new DoSomethingObject());
        IntStream.range(0, 100).forEach(
                x -> {
                    Thread t = new Thread(objectPool::exec, "Thread " + x );
                    System.out.println(t.getName() + " is created");
                    t.start();
                }
        );
    }
}
