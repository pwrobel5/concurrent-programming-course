package producers_consumers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private String message;

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    public Buffer() {
        this.message = null;
    }

    public void put(String message) {
        lock.lock();
        try {
            while(this.message != null)
                notFull.await();

            this.message = message;
            System.out.println("Put " + message);
            notEmpty.signal();
        } catch(InterruptedException e) {
            System.out.println("Put interrupted");
        } finally {
            lock.unlock();
        }
    }

    public String take() {
        lock.lock();
        String result = "";

        try {
            while(this.message == null)
                notEmpty.await();

            result = this.message;
            System.out.println("Received " + result);
            this.message = null;
            notFull.signal();
        } catch (InterruptedException e) {
            System.out.println("Take interrupted");
        } finally {
            lock.unlock();
        }
        return result;
    }

    public void isEmpty() {
        System.out.println((this.message == null) ? "Empty" : "Non-empty");
    }
}