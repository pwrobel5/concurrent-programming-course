import race.Buffer;
import race.Decrementator;
import race.Incrementator;
import race.BinarySemaphore;
import shop.Client;
import shop.GeneralSemaphore;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting shop simulation");

        GeneralSemaphore shopBaskets = new GeneralSemaphore(5);
        Client[] clients = new Client[10];

        for(int i = 0; i < clients.length; i++) {
            clients[i] = new Client(i, shopBaskets);
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++) {
            try {
                clients[i].join();
            } catch(InterruptedException e) {}
        }

        System.out.printf("Shop simulation finished");

        /*
        Buffer buffer = new Buffer(0);
        BinarySemaphore semaphore = new BinarySemaphore();
        Incrementator incrementator = new Incrementator(semaphore, buffer);
        Decrementator decrementator = new Decrementator(semaphore, buffer);

        incrementator.start();
        decrementator.start();

        try {
            incrementator.join();
            decrementator.join();
        } catch (InterruptedException e) {}

        buffer.printState();
        */
    }
}
