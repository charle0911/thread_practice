package topic.one;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Hi my name is Monster Lee");
    }

    public static void main(String[] args) {
        MyThread mt = new MyThread();
        mt.start();
    }
}
