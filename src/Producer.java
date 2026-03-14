import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<WorkItem> buffer;
    private final int[][] A;
    private final int[][] B;
    private final int splitSize;
    private final int numConsumers;
    private final int maxProducerSleepTime;

    private final Random rand = new Random();

    public Producer(BlockingQueue<WorkItem> buffer,
                    int[][] A,
                    int[][] B,
                    int splitSize,
                    int numConsumers,
                    int maxProducerSleepTime) {

        this.buffer = buffer;
        this.A = A;
        this.B = B;
        this.splitSize = splitSize;
        this.numConsumers = numConsumers;
        this.maxProducerSleepTime = maxProducerSleepTime;
    }

    @Override
    public void run() {

        try {

            for (int i = 0; i < A.length; i += splitSize) {

                int rowSize = Math.min(splitSize, A.length - i);

                // build subA
                int[][] subA = new int[rowSize][A[0].length];
                for (int r = 0; r < rowSize; r++) {
                    for (int c = 0; c < A[0].length; c++) {
                        subA[r][c] = A[i + r][c];
                    }
                }

                for (int j = 0; j < B[0].length; j += splitSize) {

                    int colSize = Math.min(splitSize, B[0].length - j);

                    // build subB
                    int[][] subB = new int[B.length][colSize];
                    for (int r = 0; r < B.length; r++) {
                        for (int c = 0; c < colSize; c++) {
                            subB[r][c] = B[r][j + c];
                        }
                    }

                    WorkItem item = new WorkItem();

                    item.setSubA(subA);
                    item.setSubB(subB);

                    item.setLowA(i);
                    item.setHighA(i + rowSize - 1);

                    item.setLowB(j);
                    item.setHighB(j + colSize - 1);

                    item.setDone(false);

                    buffer.put(item);

                    System.out.println(
                            "Producer produced block A[" +
                                    item.getLowA() + "-" + item.getHighA() +
                                    "] B[" +
                                    item.getLowB() + "-" + item.getHighB() + "]"
                    );

                    Thread.sleep(rand.nextInt(maxProducerSleepTime));
                }
            }

            // poison pills
            for (int i = 0; i < numConsumers; i++) {
                WorkItem poison = new WorkItem();
                poison.setDone(true);
                buffer.put(poison);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}