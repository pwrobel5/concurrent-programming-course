package buffer_pipe;

public class Processor implements Runnable {
    private int producedValue;
    private Buffer buffer;
    private final int CYCLES = 5;
    private int waitingTime;
    private int processorID;

    public Processor(int producedValue, Buffer buffer, int processorID) {
        this.producedValue = producedValue;
        this.buffer = buffer;
        this.waitingTime = (int) Math.random() * 10000;
        this.processorID = processorID;
    }


    @Override
    public void run() {
        for(int i = 0; i < CYCLES; i++) {
            buffer.process(processorID, producedValue, waitingTime);
        }
    }
}
