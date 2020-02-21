package table;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter {
    private final Lock lock = new ReentrantLock();
    private final Condition tableFree = lock.newCondition();
    private final Condition bothPeopleSitting = lock.newCondition();

    private Condition[] clientPairs;
    private int[] peopleToCompletePair;
    private int peopleAtTheTable;
    private int pairAtTheTable;
    private boolean isTableFree;

    private ArrayList<Integer> completePairNotification;
    private ArrayList<Integer> reservedTableNotification;

    public Waiter(int numberOfPairs) {
        clientPairs = new Condition[numberOfPairs];
        peopleToCompletePair = new int[numberOfPairs];
        peopleAtTheTable = 0;
        pairAtTheTable = -1;
        isTableFree = true;

        completePairNotification = new ArrayList<>();
        reservedTableNotification = new ArrayList<>();

        for(int i = 0; i < numberOfPairs; i++) {
            clientPairs[i] = lock.newCondition();
            peopleToCompletePair[i] = 2;
        }
    }

    public void acceptReservation(int pairID) {
        lock.lock();

        peopleToCompletePair[pairID]--;
        clientPairs[pairID].signal();

        try {
            while (peopleToCompletePair[pairID] != 0)
                clientPairs[pairID].await();
        } catch (InterruptedException e) {
            System.out.println("Reserving interrupted");
        }

        if(!completePairNotification.contains(pairID)) {
            System.out.println("[WAITER] Both people from pair " + pairID + " came to reserve place, waiting for free table...");
            completePairNotification.add(pairID);
        }

        try {
            while (isTableFree != true && pairAtTheTable != pairID)
                tableFree.await();

        } catch (InterruptedException e) {
            System.out.println("Waiting for free table interrupted");
        }

        isTableFree = false;
        pairAtTheTable = pairID;
        tableFree.signalAll();

        if(!reservedTableNotification.contains(pairID)) {
            System.out.println("[WAITER] Table given to pair " + pairID);
            reservedTableNotification.add(pairID);
        }
        peopleAtTheTable++;
        bothPeopleSitting.signal();

        try {
            while (peopleAtTheTable != 2)
                bothPeopleSitting.await();
        } catch (InterruptedException e) {}

        lock.unlock();
    }

    public void acceptLeaving() {
        lock.lock();

        System.out.println("[WAITER] Person from pair " + pairAtTheTable + " left the table");
        peopleAtTheTable--;
        if(peopleAtTheTable == 0) {
            isTableFree = true;
            tableFree.signalAll();
        }

        lock.unlock();
    }
}
