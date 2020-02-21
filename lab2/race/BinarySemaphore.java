package race;

public class BinarySemaphore{
    private boolean raised;

    public BinarySemaphore() {
        this.raised = true;
    }

    public synchronized void raise() {
        this.raised = true;
        notifyAll();
    }

    public synchronized void lower() {
        while(!this.raised) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        this.raised = false;
    }
}
