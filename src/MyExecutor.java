import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                for (int y = 0; y < 5; y++) {
                    System.out.println("Hi my name is Monster Lee" + y);
                }
            });
        }
        executorService.shutdown();
    }
}
