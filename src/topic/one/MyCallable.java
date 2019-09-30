package topic.one;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Hi my name is Monster Lee";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable mc = new MyCallable();
        FutureTask<String> ftData = new FutureTask<>(mc);
        Thread thread = new Thread(ftData);
        thread.run();
        System.out.println(ftData.get());
    }
}
