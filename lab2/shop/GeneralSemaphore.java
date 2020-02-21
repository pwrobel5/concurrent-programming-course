package shop;

public class GeneralSemaphore {

    private int state;

    public GeneralSemaphore(int state) {
        this.state = state;
    }

    public synchronized void raise(int clientID) {
        this.state++;
        System.out.println("Client " + clientID + " returned basket, baskets left: " + this.state);
        notifyAll();
    }

    public synchronized void lower(int clientID) {
        while(this.state == 0) {
            try {
                wait();
            } catch(InterruptedException e) {}
        }

        this.state--;
        System.out.println("Client " + clientID + " took one basket, baskets left: " + this.state);
    }
}
