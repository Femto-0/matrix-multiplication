import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<WorkItem> buffer;
    private final NormalMatrixMultiplication multiplier;
    private final int[][] matrixC;
    private final int maxConsumerSleepTime;

    private int threadSleepTime;
    private long executionTime;

    private int itemsConsumed;
    private int bufferEmptyCount;

    private final Random rand = new Random();
    PrintMatrix printMatrix;

    public Consumer(BlockingQueue<WorkItem> buffer, NormalMatrixMultiplication multiplier, int[][] matrixC, int maxConsumerSleepTime) {
        this.buffer = buffer;
        this.multiplier = multiplier;
        this.matrixC = matrixC;
        this.maxConsumerSleepTime = maxConsumerSleepTime;
    }

    public int getThreadSleepTime(){
        return threadSleepTime;
    }
    public long getExecutionTime(){
        return executionTime;
    }
    public int getItemsConsumed(){
        return itemsConsumed;
    }
    public int getBufferEmptyCount(){
        return bufferEmptyCount;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                if(buffer.isEmpty()){
                    bufferEmptyCount++;
                    System.out.println("The queue is empty. Consumer : "+ Thread.currentThread().getName()+" is waiting...");
                }

                WorkItem item = buffer.take();

                if (item.isDone()) {
                    System.out.println("Consumer: "+Thread.currentThread().getName() + " exiting");
                    break;
                }
                WorkItem workItem=multiplier.matrixMultiplication(item.getSubA(), item.getSubB());
                int[][] subC = workItem.getMatrix();

                /*
                The output is out of order because of following three lines. need to work on this
                 */
                String name= Thread.currentThread().getName()+" finished multiplying";
                printMatrix= new PrintMatrix(subC, name);
                printMatrix.printMatrix();

                //place subC into final matrix C
                for (int i = 0; i < subC.length; i++) {
                    System.arraycopy(subC[i], 0, matrixC[item.getLowA() + i], item.getLowB(), subC[0].length);
                }

                System.out.println(
                        "Consumer "+
                            Thread.currentThread().getName() +
                                " inserted block into C rows [" +
                                item.getLowA() + "-" + item.getHighA() +
                                "] cols [" +
                                item.getLowB() + "-" + item.getHighB() + "]"
                );
                itemsConsumed++;
                int sleep= rand.nextInt(maxConsumerSleepTime);
                threadSleepTime +=sleep;
                Thread.sleep(sleep);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTime= System.currentTimeMillis();
        executionTime=endTime- startTime;
    }

}