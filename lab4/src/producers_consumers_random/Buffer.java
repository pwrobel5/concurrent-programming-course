package producers_consumers_random;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    private int[] bufferContent;
    private int bufferSize;
    private int freePlace;
    private int currentPosition;

    private int producersNumber;
    private int consumersNumber;

    private final Lock lock = new ReentrantLock();
    private final Condition waitingConsumer = lock.newCondition();
    private final Condition waitingProducer = lock.newCondition();

    public Buffer(int M, int producersNumber, int consumersNumber) {
        this.bufferSize = 2 * M;
        this.freePlace = 2 * M;
        this.currentPosition = 0;
        this.producersNumber = producersNumber;
        this.consumersNumber = consumersNumber;

        this.bufferContent = new int[this.bufferSize];
        for(int i = 0; i < this.bufferSize; i++) {
            this.bufferContent[i] = -1;
        }
    }

    @Override
    public void consumeMElements(int consumerID, int M) {
        lock.lock();

        while(bufferSize - freePlace < M) {
            try {
                if(producersNumber == 0) {
                    lock.unlock();
                    return;
                }
                waitingConsumer.await();
            } catch (InterruptedException e) {}
        }

        for(int i = 0; i < M; i++) {
            currentPosition = (currentPosition == 0) ? (bufferSize - 1) : (currentPosition - 1);
            bufferContent[currentPosition] = -1;
            freePlace++;
        }
        System.out.println("[CONSUMER " + consumerID + "] " + M + " elements taken");
        consumersNumber--;
        waitingProducer.signalAll();

        lock.unlock();
    }

    @Override
    public void produceMElements(int producentID, int M, int value) {
        lock.lock();

        while(freePlace < M) {
            try {
                if(consumersNumber == 0) {
                    lock.unlock();
                    return;
                }
                waitingProducer.await();
            } catch (InterruptedException e) {}
        }

        for(int i = 0; i < M; i++) {
            bufferContent[currentPosition] = value;
            currentPosition = (currentPosition + 1) % bufferSize;
            freePlace--;
        }
        System.out.println("[PRODUCER " + producentID + "] " + M + " elements changed into " + value);
        producersNumber--;
        waitingConsumer.signalAll();

        lock.unlock();
    }
}
