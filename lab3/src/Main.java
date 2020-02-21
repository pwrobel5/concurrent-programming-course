import printers.PrinterClient;
import printers.PrinterMonitor;
import producers_consumers.Buffer;
import producers_consumers.Consumer;
import producers_consumers.Producer;
import table.PairPerson;
import table.Waiter;

public class Main {
    public static void main(String[] args) {
        /* // producers/consumers
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
        System.out.print("producenci_konsumenci.Buffer status: ");
        buffer.isEmpty();
        */
/*
        // printers

        int N = 10;
        int M = 6;

        PrinterMonitor printerMonitor = new PrinterMonitor(M);
        PrinterClient printerClients[] = new PrinterClient[N];

        for(int i = 0; i < N; i++) {
            printerClients[i] = new PrinterClient(i, printerMonitor);
        }

        Thread printerClientThreads[] = new Thread[N];
        for(int i = 0; i < N; i++) {
            printerClientThreads[i] = new Thread(printerClients[i]);
            printerClientThreads[i].start();
        }

        for(int i = 0; i < N; i++) {
            try {
                printerClientThreads[i].join();
            } catch(InterruptedException e) {}
        }

        System.out.println("Successfully ended");*/

        // table
        int N = 10;
        Waiter waiter = new Waiter(N);
        PairPerson[] people = new PairPerson[2 * N];
        for(int i = 0; i < N; i++) {
            people[2 * i] = new PairPerson(i, 1, waiter);
            people[2 * i + 1] = new PairPerson(i, 2, waiter);
        }

        Thread[] pairPeopleThreads = new Thread[2 * N];
        for(int i = 0; i < 2 * N; i++) {
            pairPeopleThreads[i] = new Thread(people[i]);
            pairPeopleThreads[i].start();
        }

        for(int i = 0; i < 2 * N; i++) {
            try {
                pairPeopleThreads[i].join();
            } catch(InterruptedException e) {}
        }

        System.out.println("[MAIN] Waiter session successfully ended");

    }
}