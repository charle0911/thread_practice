package topic.one;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
            System.out.println("Hi my name is Monster Lee");
        }

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.run();
    }
}
