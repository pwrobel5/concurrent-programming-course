package producers_consumers_random;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Producer implements Runnable {
    private int producerID;
    private IBuffer buffer;
    private int M;
    private int takenElements;
    private int value;
    private String outputFileName;

    public Producer(int producerID, IBuffer buffer, int m, int value, String outputFileName) {
        this.producerID = producerID;
        this.buffer = buffer;
        M = m;
        this.value = value;
        this.outputFileName = outputFileName;

        this.takenElements = (int)(Math.random() * M) + 1;
    }

    @Override
    public void run() {
        long timeBeforeRun = System.nanoTime();
        buffer.produceMElements(producerID, takenElements, value);
        long timeAfterRun = System.nanoTime();
        long timeDifference = timeAfterRun - timeBeforeRun;

        try {
            PrintWriter producerPrintWriter = new PrintWriter(new FileWriter(outputFileName, true));
            producerPrintWriter.printf("%d %d\n", takenElements, timeDifference);
            producerPrintWriter.close();
        } catch (IOException e) {}
    }
}
