package topic.one;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
            System.out.println("Hi my name is Monster Lee");
        }

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.start();

        Thread thread1 = new Thread(() -> {
            System.out.println("Hi I am lambda runnable");
        });
        thread1.start();
    }
}
