package buffer_pipe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private int[] bufferContent;

    private final Lock lock = new ReentrantLock();
    private int[] cellsToProcess;
    private Condition[] notifyProcessors;

    private int processorsNumber;
    private int bufferSize;

    public Buffer(int bufferSize, int processorsNumber) {
        this.bufferSize = bufferSize;
        this.processorsNumber = processorsNumber;

        this.bufferContent = new int[bufferSize];
        this.cellsToProcess = new int[processorsNumber];
        this.notifyProcessors = new Condition[processorsNumber];

        for(int i = 0; i < bufferSize; i++) {
            this.bufferContent[i] = -1;
        }

        for(int i = 0; i < processorsNumber; i++) {
            this.cellsToProcess[i] = 0;
            this.notifyProcessors[i] = lock.newCondition();
        }

        this.cellsToProcess[0] = bufferSize;
    }

    public void process(int processorID, int value, int waitingTime) {
        lock.lock();

        int processedCells = 0;
        int currentCell = 0;

        while(processedCells < bufferSize) {
            while(cellsToProcess[processorID] == 0) {
                try {
                    notifyProcessors[processorID].await();
                } catch (InterruptedException e) { }
            }

            cellsToProcess[processorID]--;
            processedCells++;
            bufferContent[currentCell] = value;
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {}

            if(processorID == 0) {
                System.out.print("[PRODUCER] ");
            } else if(processorID == processorsNumber - 1) {
                System.out.print("[CONSUMER] ");
            } else {
                System.out.print("[PROCESSOR] " + processorID + " ");
            }
            System.out.println("Value " + value + " inserted in position " + currentCell);

            currentCell = (currentCell + 1) % bufferSize;
            cellsToProcess[(processorID + 1) % processorsNumber]++;
            notifyProcessors[(processorID + 1) % processorsNumber].signal();
        }

        lock.unlock();
    }
}
