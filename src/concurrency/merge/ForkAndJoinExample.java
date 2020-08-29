package concurrency.merge;

public class ForkAndJoinExample {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("T1 task start");
                // do something that need long time
                Thread.sleep(4000);
                System.out.println("T1 task finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("T2 task start");
                // do something that need long time
                Thread.sleep(2000);
                System.out.println("T2 task finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Start t1 and t2 task");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("T1 task and T2 task are all already");
        System.out.println("Do the main thread task");
    }
}
