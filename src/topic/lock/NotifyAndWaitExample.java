package topic.lock;

import java.util.ArrayList;
import java.util.List;

class NewAllocator {
    private static List<Object> lockList = new ArrayList<>();

    synchronized public boolean apply(Object source, Object target) {
        while (lockList.contains(source) || lockList.contains(target)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lockList.add(source);
        lockList.add(target);
        return true;
    }

    synchronized public void free(Object source, Object target) {
        lockList.remove(source);
        lockList.remove(target);
        notifyAll();
    }

}

public class NotifyAndWaitExample {

    private int balance = 10000;
    private static Allocator allocator = new Allocator();

    private final Object balanceLock = new Object();

    // Withdraw money from account
    int withdraw(int amt) {
        synchronized (balanceLock) {
            if (this.balance > amt) {
                this.balance -= amt;
                return this.balance;
            }
            return 0;
        }
    }

    void transfer(AccountWithAllocate source, int amt) {
        while (!allocator.apply(source, this)) ;

        int money = source.withdraw(amt);
        if (money > 0)
            this.balance += amt;
        allocator.free(source, this);
    }

}

