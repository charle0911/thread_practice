package topic.one;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(4000);
        return "Hi my name is Monster Lee";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable mc = new MyCallable();
        FutureTask<String> ftData = new FutureTask<>(mc);
        Thread thread = new Thread(ftData);
        thread.start();
//        while (!ftData.isDone()) ;
        System.out.println(ftData.get());

        FutureTask<String> ftData2 = new FutureTask<String>(()-> "in lambda callable");
        Thread thread1 = new Thread(ftData2);
        thread1.start();
        while (!ftData2.isDone()) ;
        System.out.println(ftData2.get());
    }
}
