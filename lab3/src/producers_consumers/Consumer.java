package producers_consumers;

public class Consumer implements Runnable {
    private Buffer buffer;
    private int consumerNumber;
    private final int ILOSC = 10;

    public Consumer(int consumerNumber, Buffer buffer) {
        this.buffer = buffer;
        this.consumerNumber = consumerNumber;
    }

    public void run() {

        for(int i = 0;  i < ILOSC;   i++) {
            String message = buffer.take();
        }

    }
}
