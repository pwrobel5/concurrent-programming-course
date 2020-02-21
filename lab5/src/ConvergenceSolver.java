import java.util.HashMap;
import java.util.concurrent.Callable;

public class ConvergenceSolver implements Callable {
    private int width;
    private int height;
    private int offset;
    private int pixels;

    private final int ZOOM = 150;

    private final int MAX_ITER = 570;

    public ConvergenceSolver(int width, int height, int offset, int pixels) {
        this.width = width;
        this.height = height;
        this.offset = offset;
        this.pixels = pixels;
    }

    @Override
    public HashMap<Pair, Integer> call() throws Exception {
        HashMap<Pair, Integer> result = new HashMap<>();

        for(int i = 0; i < pixels; i++) {
            int x = (offset + i) % width;
            int y = (offset + i) / width;

            double cX = ((double) (x - 400)) / ZOOM;
            double cY = ((double) (y - 300)) / ZOOM;

            double zx = 0;
            double zy = 0;
            double tmp;

            int iter = MAX_ITER;
            while (zx * zx + zy * zy < 4 && iter > 0) {
                tmp = zx * zx - zy * zy + cX;
                zy = 2.0 * zx * zy + cY;
                zx = tmp;
                iter--;
            }

            result.put(new Pair(x, y), iter);
        }
        return result;
    }
}
