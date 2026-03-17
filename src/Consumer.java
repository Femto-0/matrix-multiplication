import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<WorkItem> buffer;
    private final NormalMatrixMultiplication multiplier;
    private final int[][] matrixC;
    private final int maxConsumerSleepTime;

    private final Random rand = new Random();

    public Consumer(BlockingQueue<WorkItem> buffer,
                    NormalMatrixMultiplication multiplier,
                    int[][] matrixC,
                    int maxConsumerSleepTime) {

        this.buffer = buffer;
        this.multiplier = multiplier;
        this.matrixC = matrixC;
        this.maxConsumerSleepTime = maxConsumerSleepTime;
    }

    @Override
    public void run() {

        try {

            while (true) {

                WorkItem item = buffer.take();

                if (item.isDone()) {
                    System.out.println(Thread.currentThread().getName() + " exiting");
                    break;
                }
                WorkItem workItem=multiplier.matrixMultiplication(item.getSubA(), item.getSubB());
                int[][] subC = workItem.getMatrix();

                //place subC into final matrix C
                for (int i = 0; i < subC.length; i++) {
                    for (int j = 0; j < subC[0].length; j++) {

                        matrixC[item.getLowA() + i][item.getLowB() + j] = subC[i][j];
                    }
                }

                System.out.println(
                        Thread.currentThread().getName() +
                                " inserted block into C rows [" +
                                item.getLowA() + "-" + item.getHighA() +
                                "] cols [" +
                                item.getLowB() + "-" + item.getHighB() + "]"
                );

                Thread.sleep(rand.nextInt(maxConsumerSleepTime));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}