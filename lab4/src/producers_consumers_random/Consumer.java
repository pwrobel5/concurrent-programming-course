package producers_consumers_random;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Consumer implements Runnable {
    private int consumerID;
    private IBuffer buffer;
    private int M;
    private int takenElements;
    private String outputFileName;

    public Consumer(int consumerID, IBuffer buffer, int m, String outputFileName) {
        this.consumerID = consumerID;
        this.buffer = buffer;
        M = m;
        this.outputFileName = outputFileName;

        this.takenElements = (int)(Math.random() * M) + 1;
    }

    @Override
    public void run() {
        long timeBeforeRun = System.nanoTime();
        buffer.consumeMElements(consumerID, takenElements);
        long timeAfterRun = System.nanoTime();
        long timeDifference = timeAfterRun - timeBeforeRun;

        try {
            PrintWriter producerPrintWriter = new PrintWriter(new FileWriter(outputFileName, true));
            producerPrintWriter.printf("%d %d\n", takenElements, timeDifference);
            producerPrintWriter.close();
        } catch (IOException e) {}
    }
}
