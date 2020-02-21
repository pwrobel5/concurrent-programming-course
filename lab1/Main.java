import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        /*
        for(int i = 0; i < 10; i++) {
            Counter counter = new Counter(0);
            Incrementator incrementator = new Incrementator(counter);
            Decrementator decrementator = new Decrementator(counter);

            incrementator.start();
            decrementator.start();

            try {
                incrementator.join();
                decrementator.join();
            } catch(InterruptedException e) {
                System.out.println();
            }

            System.out.print("Iteration " + i + ": ");
            counter.printCounter();
        }*/


        Buffer buffer = new Buffer();
        Consumer consumers[] = new Consumer[5];
        Producer producers[] = new Producer[5];

        for(int i = 0; i < 5; i++) {
            consumers[i] = new Consumer(i, buffer);
            producers[i] = new Producer(i, buffer);
        }

        Thread consumerThreads[] = new Thread[5];
        Thread producerThreads[] = new Thread[5];
        for(int i = 0; i < 5; i++) {
            consumerThreads[i] = new Thread(consumers[i]);
            consumerThreads[i].start();
        }

        for(int i = 0; i < 5; i++) {
            producerThreads[i] = new Thread(producers[i]);
            producerThreads[i].start();
        }

        for(int i = 0; i < 5; i++) {
            try {
                consumerThreads[i].join();
                producerThreads[i].join();
            } catch(InterruptedException e) {}
        }

        System.out.println("Successfully ended");
    }
}
