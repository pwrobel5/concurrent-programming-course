package printers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterMonitor {
    private boolean[] printers;
    private int busyPrinters;

    private final Lock lock = new ReentrantLock();
    private final Condition freePrinter = lock.newCondition();

    public PrinterMonitor(int numberOfPrinters) {
        printers = new boolean[numberOfPrinters];
        for(int i = 0; i < numberOfPrinters; i++) {
            printers[i] = true;
        }

        busyPrinters = 0;
    }

    public int reservePrinter() {
        lock.lock();
        int freePrinterIndex = 0;

        try {
            while(busyPrinters == printers.length)
                freePrinter.await();

            while(printers[freePrinterIndex] != true)
                freePrinterIndex++;

            printers[freePrinterIndex] = false;
            System.out.println("[MONITOR] Reserved printer number " + freePrinterIndex);
            busyPrinters++;
        } catch(InterruptedException e) {
            System.out.println("Reserving interrupted");
        } finally {
            lock.unlock();
        }

        return freePrinterIndex;
    }

    public void releasePrinter(int printerNumber) {
        lock.lock();
        printers[printerNumber] = true;
        busyPrinters--;
        System.out.println("[MONITOR] Printer " + printerNumber + " is now free");
        freePrinter.signal();

        lock.unlock();
    }
}
