public class Decrementator extends Thread {
    private Counter counter;
    private final int DEC_COUNT = 10000000;

    public Decrementator(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for(int i = 0; i < DEC_COUNT; i++) {
            this.counter.decrementCounter();
        }
    }
}
