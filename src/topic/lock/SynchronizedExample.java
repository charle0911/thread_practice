package topic.lock;

public class SynchronizedExample {
    private int cnt = 0;

    public synchronized int getCnt() {
        return cnt;
    }

    public synchronized void addCnt() {
        cnt++;
    }

    public static void main(String[] args) throws InterruptedException {
        final SynchronizedExample synchronizedExample = new SynchronizedExample();
        for (int i = 0; i < 10; i++) {
             new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    synchronizedExample.addCnt();
                }
                System.out.println(Thread.currentThread().getName() + " - Add finish");
            }, "Thread - " + i).start();
        }

        // Wait for thread finish their job
        Thread.sleep(1000);

        System.out.println(synchronizedExample.getCnt());
    }
}
