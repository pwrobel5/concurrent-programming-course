package producers_consumers;

public class Producer implements Runnable {
    private Buffer buffer;
    private int producerNumber;
    private final int ILOSC = 10;

    public Producer(int producerNumber, Buffer buffer) {
        this.buffer = buffer;
        this.producerNumber = producerNumber;
    }

    public void run() {

        for(int i = 0;  i < ILOSC;   i++) {
            buffer.put("message from producer " + this.producerNumber + ":" + i);
        }

    }
}