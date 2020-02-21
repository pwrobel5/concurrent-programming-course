package table;

public class PairPerson implements Runnable {
    private int pairID;
    private int individualID;
    private int ownAffairsTime;
    private int eatingTime;

    private Waiter waiter;

    public PairPerson(int pairID, int individualID, Waiter waiter) {
        this.pairID = pairID;
        this.individualID = individualID;
        this.ownAffairsTime = (int) Math.random() * 10 + 5;
        this.eatingTime = (int) Math.random() * 10 + 8;
        this.waiter = waiter;
    }

    private void ownAffairs() {
        System.out.println("[PAIR " + pairID + "] Person " + individualID + " is doing something");
        try {
            Thread.sleep(ownAffairsTime);
        } catch (InterruptedException e) { }
    }

    private void eat() {
        System.out.println("[PAIR " + pairID + "] Person " + individualID + " started eating");
        try {
            Thread.sleep(eatingTime);
        } catch (InterruptedException e) { }
        System.out.println("[PAIR " + pairID + "] Person " + individualID + " ended eating");
    }

    public void run() {
        ownAffairs();
        waiter.acceptReservation(pairID);
        eat();
        waiter.acceptLeaving();
    }
}
