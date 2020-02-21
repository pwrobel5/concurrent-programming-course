public class Counter {
    private int counter;

    public Counter(int counter) {
        this.counter = counter;
    }

    synchronized public void incrementCounter() {
        this.counter++;
    }

    synchronized public void decrementCounter() {
        this.counter--;
    }

    public void printCounter() {
        System.out.println("Actual counter: " + this.counter);
    }
}
