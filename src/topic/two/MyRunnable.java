package topic.two;

public class MyRunnable implements Runnable {
    private int ticket = 5;
    public void run(){
        for (int i=0;i<10;i++)
        {
            if(ticket > 0){
                System.out.println("ticket = " + ticket--);
            }
        }
    }

    public static void main(String[] args){
        MyRunnable my = new MyRunnable();
        new Thread(my).start();
        new Thread(my).start();
        new Thread(my).start();
    }
}
