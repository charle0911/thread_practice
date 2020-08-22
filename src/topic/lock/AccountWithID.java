package topic.lock;

import java.util.Random;

public class AccountWithID {

    public int id = (int) (Math.random() * 100);
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

    void transfer(AccountWithID source, int amt) {
        AccountWithID higher;
        AccountWithID lower;
        if (this.id > source.id) {
            higher = this;
            lower = source;
        } else {
            higher = source;
            lower = this;
        }
        synchronized (higher) {
            synchronized (lower) {
                int money = source.withdraw(amt);
                if (money > 0)
                    this.balance += amt;
                allocator.free(source, this);
            }
        }
    }

}
