package concurrency.seperate;

import java.time.LocalTime;
import java.util.concurrent.*;

class UserRating {
    String id;
    String rate;
}

class UserInfo {
    CompletableFuture<UserRating> getRating() {
        // do some db job
        return CompletableFuture.completedFuture(new UserRating());
    }
}

class UserDataService {
    CompletableFuture<UserInfo> getUserInfo(String id) {
        // do some db job
        return CompletableFuture.completedFuture(new UserInfo());
    }

    CompletableFuture<UserRating> getUserRating(UserInfo info) {
        return info.getRating();
    }
}


public class CompletableFutureExample {
    LocalTime now;

    public static void printThread(String functionName) {
        System.out.println("Function : " + functionName + " is running on " + Thread.currentThread().toString());
    }

    public void basicTest() throws ExecutionException, InterruptedException {
        // Basic Use
        now = LocalTime.now();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Supply Async";
        });
        System.out.println(LocalTime.now().toSecondOfDay() - now.toSecondOfDay());
        Executor threadPool = Executors.newCachedThreadPool();
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Supply Async";
        }, threadPool);
        System.out.println(LocalTime.now().toSecondOfDay() - now.toSecondOfDay());
        System.out.println(f2.join());
        System.out.println(LocalTime.now().toSecondOfDay() - now.toSecondOfDay());
        System.out.println(f1.get());
        System.out.println(LocalTime.now().toSecondOfDay() - now.toSecondOfDay());

    }

    public void serialTest() throws ExecutionException, InterruptedException {
        // 串行
        now = LocalTime.now();
        Executor threadPool = Executors.newFixedThreadPool(10);

        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> {
            CompletableFutureExample.printThread("f3");
        }, threadPool);
        f3.thenRun(() -> CompletableFutureExample.printThread("f3"));
        f3.thenRunAsync(() -> CompletableFutureExample.printThread("f3"));


        CompletableFuture<Void> f4 = CompletableFuture.runAsync(() -> {
            CompletableFutureExample.printThread("f4");
        }, threadPool);
        f4.thenRunAsync(() -> CompletableFutureExample.printThread("f4"));
        f4.thenRunAsync(() -> CompletableFutureExample.printThread("f4"));

    }

    public void composeAndApplyTest() {
        UserDataService userDataService = new UserDataService();
        CompletableFuture<CompletableFuture<UserRating>> applyTest =
                userDataService.getUserInfo("1")
                        .thenApply(userDataService::getUserRating);
        CompletableFuture<UserRating> composeTest =
                userDataService.getUserInfo("1")
                        .thenCompose(userDataService::getUserRating);

    }

    public void andTest() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "f1Result");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "f2Result");
        CompletableFuture<String> f3 = f1.thenCombine(f2, (x, y) -> x + y);
        CompletableFuture<Void> f4 = f1.thenAcceptBoth(f2, (x, y) -> {
            System.out.println("In consumer");
        });
        System.out.println(f3.join());
    }

    public void orTest() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f1Result";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2Result";
        });
        CompletableFuture<String> f3 = f1.applyToEither(f2, x -> x);
        System.out.println(f3.join());
    }

    public void exceptionAndFinallyTest() {
        CompletableFuture<Integer> f1 =
                CompletableFuture.supplyAsync(() -> 1 / 0)
                        .thenApply(x -> x * 10)
                        .exceptionally(e -> {
                            System.out.println("This method has exception so return 0");
                            return 0;
                        });
        CompletableFuture<Integer> f2 =
                CompletableFuture.supplyAsync(() -> {
                    return 1 / 0;
                })
                        .thenApply(x -> x * 10)
                        .whenComplete((x, e) -> {
                            CompletableFutureExample.printThread("exceptionAndFinallyTest");
                            System.out.println("f2 in complete");
                            e.printStackTrace();
                        });
        CompletableFuture<Integer> f3 =
                CompletableFuture.supplyAsync(() -> {
                    return 1 / 0;
                })
                        .thenApply(x -> x * 10)
                        .whenCompleteAsync((x, e) -> {
                            System.out.println("f3 in complete");
                            CompletableFutureExample.printThread("exceptionAndFinallyTest");
                            e.printStackTrace();
                        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        LocalTime now = LocalTime.now();
        CompletableFutureExample completableFutureExample = new CompletableFutureExample();
//        completableFutureExample.serialTest();
//        completableFutureExample.andTest();
//        completableFutureExample.orTest();
        completableFutureExample.exceptionAndFinallyTest();

    }
}
