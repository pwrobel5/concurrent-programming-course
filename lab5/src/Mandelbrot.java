import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JFrame;

// based on http://rosettacode.org/wiki/Mandelbrot_set#Java

public class Mandelbrot extends JFrame {

    private BufferedImage I;

    private final int THREADS = 8;
    private final int TASKS = (800 * 600) / THREADS;

    public Mandelbrot() throws Exception {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());

        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        int blockSize = (getHeight() * getWidth()) / (TASKS * THREADS);
        LinkedList<Future> calculatedMaps = new LinkedList<>();

        long startingTime = System.nanoTime();

        for(int i = 0; i < THREADS * TASKS; i++) {
                Callable<HashMap<Pair, Integer>> callable = new ConvergenceSolver(getWidth(), getHeight(), i * blockSize, blockSize);
                calculatedMaps.add(pool.submit(callable));
        }

        for(Future<HashMap> futureMap : calculatedMaps) {
            HashMap<Pair, Integer> currentMap = futureMap.get();
            for(Pair key : currentMap.keySet()) {
                Integer x = (Integer) key.getFirst();
                Integer y = (Integer) key.getSecond();

                Integer iter = currentMap.get(key);
                I.setRGB(x, y, iter | (iter << 8));
            }
        }

        long endingTime = System.nanoTime();
        long passedTime = endingTime - startingTime;

        String outputFileName = "mandelbrot.dat";

        /*
        try {
            PrintWriter mandelbrotPrintWriter = new PrintWriter(new FileWriter(outputFileName, true));
            mandelbrotPrintWriter.printf("%d %d %d\n", THREADS, TASKS, passedTime);
            mandelbrotPrintWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) throws Exception {
        new Mandelbrot().setVisible(true);
    }
}
