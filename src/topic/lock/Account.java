package topic.lock;

/**
 * This is an example talking about rough and fine lock
 *
 * @author : Charlie Lee
 * @codeby : IDEA
 * @since : 2020/7/5
 */
public class Account {

    private int balance = 10000;
    private String password;

    private final Object balanceLock = new Object();
    private final Object passwordLock = new Object();

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

    // Check the balance
    int getBalance() {
        synchronized (passwordLock) {
            return balance;
        }
    }

    void transfer(Account source, int amt) {
        synchronized (balanceLock) {
            int money = source.withdraw(amt);
            if (money > 0)
                this.balance += amt;
        }
    }

    // Change the password
    void updatePassword(String pw) {
        synchronized (passwordLock) {
            this.password = pw;
        }
    }

    // Check the password
    String getPassword() {
        synchronized (passwordLock) {
            return password;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Account a1 = new Account();
        final Account a2 = new Account();

        Thread t1 = new Thread(() -> {
            a1.transfer(a2, 1000);
        });

        Thread t2 = new Thread(() -> {
            a1.updatePassword("new1234");
        });

        t1.start();
        t2.start();

        Thread.sleep(1000);

        System.out.println(a1.getBalance());
        System.out.println(a2.getBalance());
        System.out.println(a1.getPassword());
    }
}
