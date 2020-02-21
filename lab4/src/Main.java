
import buffer_pipe.Buffer;
import buffer_pipe.Processor;

import producers_consumers_random.*;

public class Main {
    public static void main(String[] args) {
        // pipeline with buffer

        int bufferSize = 10;
        int processorsNumber = 5;

        Buffer buffer = new Buffer(bufferSize, processorsNumber + 2);
        Processor[] processors = new Processor[processorsNumber + 2];

        processors[0] = new Processor(5, buffer, 0);
        processors[processorsNumber + 1] = new Processor(-1, buffer, processorsNumber + 1);

        for(int i = 1; i < processorsNumber + 1; i++) {
            processors[i] = new Processor(i, buffer, i);
        }

        Thread[] processorThreads = new Thread[processorsNumber + 2];
        for(int i = 0; i < processorsNumber + 2; i++) {
            processorThreads[i] = new Thread(processors[i]);
            processorThreads[i].start();
        }

        for(int i = 0; i < processorsNumber + 2; i++) {
            try {
                processorThreads[i].join();
            } catch (InterruptedException e) {}
        }

        System.out.println("[MAIN] Ended successfully");


        // producers, consumers - naive version
/*
        int M = 100000;
        int producersNumber = 1000;
        int consumersNumber = 1000;

        //Buffer buffer = new Buffer(M, producersNumber, consumersNumber);
        CorrectBuffer buffer = new CorrectBuffer(M, producersNumber, consumersNumber);

        Producer[] producers = new Producer[producersNumber];
        Consumer[] consumers = new Consumer[consumersNumber];

        Thread[] producersThreads = new Thread[producersNumber];
        Thread[] consumersThreads = new Thread[consumersNumber];

        for(int i = 0; i < producersNumber; i++) {
            producers[i] = new Producer(i, buffer, M, i, "producers.dat");
            producersThreads[i] = new Thread(producers[i]);
            producersThreads[i].start();
        }

        for(int i = 0; i < consumersNumber; i++) {
            consumers[i] = new Consumer(i, buffer, M, "consumers.dat");
            consumersThreads[i] = new Thread(consumers[i]);
            consumersThreads[i].start();
        }

        try {
            for(int i = 0; i < producersNumber; i++) {
                producersThreads[i].join();
            }

            for(int i = 0; i < consumersNumber; i++) {
                consumersThreads[i].join();
            }
        } catch (InterruptedException e) {}

        System.out.println("[MAIN] Ended successfully");
*/
    }
}
