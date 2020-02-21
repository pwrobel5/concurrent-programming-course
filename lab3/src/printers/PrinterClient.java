package printers;

public class PrinterClient implements Runnable {
    private int clientNumber;
    private PrinterMonitor printerMonitor;
    private final int TASKS_NUMBER = 20;

    public PrinterClient(int clientNumber, PrinterMonitor printerMonitor) {
        this.clientNumber = clientNumber;
        this.printerMonitor = printerMonitor;
    }

    private void makePrintingTask() {
        System.out.println("[CLIENT] Client " + clientNumber + " prepares printing task");
        int time = (int) Math.random() * 10 + 5;
        try {
            Thread.sleep(time * 1000);
        } catch(InterruptedException e) {}
    }

    public void print(int printerNumber) {
        System.out.println("[CLIENT] Printer number " + printerNumber + " started printing for client " + clientNumber);
        int time = (int) Math.random() * 10 + 5;
        try {
            Thread.sleep(time * 1000);
        } catch(InterruptedException e) {}
        System.out.println("[CLIENT] Printer number " + printerNumber + " finished printing for client " + clientNumber);
    }


    public void run() {
        for(int i = 0; i < TASKS_NUMBER; i++) {
            makePrintingTask();
            int printerNumber = printerMonitor.reservePrinter();
            print(printerNumber);
            printerMonitor.releasePrinter(printerNumber);
        }
    }
}
