package topic.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    private static final Lock lock = new ReentrantLock();

    private int cnt = 0;
    private String mockFile = "";

    public void addOne() {
        lock.lock();
        try {
            cnt += 1;
        } finally {
            lock.unlock();
        }
    }

    public int getCnt() {
        lock.lock();
        try {
            return cnt;
        } finally {
            lock.unlock();
        }
    }

    public void mockFileWriting(String input) {
        if (lock.tryLock()) {
            try {
                mockFile += input;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("The file is already been using");
        }
    }



    public static void main(String[] args) throws InterruptedException {
        final LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();

        System.out.println(linkedBlockingQueue.take());
    }
}
