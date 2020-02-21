package race;

public class Decrementator extends Thread {
    private BinarySemaphore semaphore;
    private Buffer buffer;
    private final int OPERATIONS_NUMBER = 10000;

    public Decrementator(BinarySemaphore semaphore, Buffer buffer) {
        this.semaphore = semaphore;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for(int i = 0; i < OPERATIONS_NUMBER; i++) {
            semaphore.lower();
            buffer.decrement();
            semaphore.raise();
        }
    }
}
