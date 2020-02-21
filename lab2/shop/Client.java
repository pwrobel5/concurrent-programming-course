package shop;

public class Client extends Thread {

    private int clientID;
    private GeneralSemaphore shop;

    public Client(int clientID, GeneralSemaphore shop) {
        this.clientID = clientID;
        this.shop = shop;
    }

    @Override
    public void run() {
        System.out.println("Client " + this.clientID + " enters shop...");
        shop.lower(this.clientID);
        int time = (int) Math.random() * 10 + 5;
        try {
            Thread.sleep(time * 1000);
        } catch(InterruptedException e) {}
        shop.raise(this.clientID);
        System.out.println("Client " + this.clientID + " leaves shop...");
    }
}
