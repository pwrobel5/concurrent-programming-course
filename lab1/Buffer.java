public class Buffer {
    private String message;

    public Buffer() {
        this.message = null;
    }

    public synchronized void put(String message) {
        while(this.message != null)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Put interrupted");
            }
        }

        this.message = message;
        System.out.println("Put " + message);
        notifyAll();
    }

    public synchronized String take() {
        while(this.message == null)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Take interrupted");
            }
        }

        String result = this.message;
        System.out.println("Received: " + message);
        this.message = null;
        notifyAll();

        return result;
    }

    public void isEmpty() {
        System.out.println((this.message == null) ? "Empty" : "Non-empty");
    }
}
