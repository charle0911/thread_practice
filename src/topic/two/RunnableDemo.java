package topic.two;

public class RunnableDemo {
    public static void main(String[] args){
        new MyThread().start();
        new MyThread().start();
        new MyThread().start();
    }
}