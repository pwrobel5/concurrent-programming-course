package producers_consumers_random;

public interface IBuffer {
    void consumeMElements(int consumerID, int M);

    void produceMElements(int producentID, int M, int value);
}
