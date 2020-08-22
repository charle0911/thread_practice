package topic.lock;

public class ObjectLockExample {
    private int cnt = 0;
    public int getCnt() {
        synchronized (this) {
            return cnt;
        }
    }

    public void addCnt() {
        synchronized (this) {
            cnt++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ObjectLockExample o1 = new ObjectLockExample();
        final ObjectLockExample o2 = new ObjectLockExample();

        for (int i = 0; i < 10; i++) {
            ObjectLockExample tmp = (i % 2 == 0) ? o1 : o2;

            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    tmp.addCnt();
                }
                System.out.println(Thread.currentThread().getName() + " - Add finish");
            }, "Thread - " + i).start();
        }

        Thread.sleep(1000);
        System.out.println(o1.getCnt());
        System.out.println(o2.getCnt());
    }
}
