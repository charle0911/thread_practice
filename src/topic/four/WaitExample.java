package topic.four;

class Producer implements Runnable {

    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        System.out.println("Start to consume");

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.check(i);
            clerk.setProduct(i);
        }
    }
}

class Consumer implements Runnable {

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        System.out.println("Start to consume");

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clerk.getProduct();
        }
    }
}


class Clerk {
    private int product = -1;

    public synchronized void setProduct(int product) {
        while (this.product != -1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.product = product;
        System.out.println("Conductor set product");
        notify();

    }

    public synchronized void check(int product) {

        while (this.product == -1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//        this.product = product;
        System.out.println("Check product");
        notify();

    }


    public synchronized int getProduct() {
        while (this.product == -1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int p = this.product;
        System.out.println("Consumer get product");

        this.product = -1;
        notify();

        return p;
    }
}


public class WaitExample {
    public static void main(String[] args) throws InterruptedException {
        Clerk clerk = new Clerk();

        Thread producerThread = new Thread(new Producer(clerk));
        Thread consumerThread = new Thread(new Consumer(clerk));

        producerThread.start();
        consumerThread.start();
    }
}

