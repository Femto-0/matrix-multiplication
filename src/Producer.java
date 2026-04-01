import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<WorkItem> buffer;
    private final int[][] A;
    private final int[][] B;
    private final int splitSize;
    private final int numConsumers;
    private final int maxProducerSleepTime;

    private int threadSleepTime;
    private long executionTime;

    private int itemsProduced;
    private int bufferFullCount;

    private final Random rand = new Random();

    public Producer(BlockingQueue<WorkItem> buffer,int[][] A, int[][] B, int splitSize, int numConsumers, int maxProducerSleepTime) {
        this.buffer = buffer;
        this.A = A;
        this.B = B;
        this.splitSize = splitSize;
        this.numConsumers = numConsumers;
        this.maxProducerSleepTime = maxProducerSleepTime;
    }


    /*
    getters and setters
     */
    public int getThreadSleepTime() {
        return threadSleepTime;
    }

    public long getExecutionTime(){
        return executionTime;
    }

    public int getItemsProduced(){
        return itemsProduced;
    }
    public int getBufferFullCount(){
        return bufferFullCount;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();

        try {

            for (int i = 0; i < A.length; i += splitSize) {

                int rowSize = Math.min(splitSize, A.length - i);

                // build subA
                int[][] subA = new int[rowSize][A[0].length];
                for (int r = 0; r < rowSize; r++) {
                    System.arraycopy(A[i + r], 0, subA[r], 0, A[0].length);
                }

                for (int j = 0; j < B[0].length; j += splitSize) {

                    int colSize = Math.min(splitSize, B[0].length - j);

                    // build subB
                    int[][] subB = new int[B.length][colSize];
                    for (int r = 0; r < B.length; r++) {
                        System.arraycopy(B[r], j, subB[r], 0, colSize);
                    }

                    WorkItem item = new WorkItem();

                    item.setSubA(subA);
                    item.setSubB(subB);

                    item.setLowA(i);
                    item.setHighA(i + rowSize - 1);

                    item.setLowB(j);
                    item.setHighB(j + colSize - 1);

                    item.setDone(false);

                    if(buffer.remainingCapacity()==0){
                        bufferFullCount++;
                    }
                    buffer.put(item);
                    itemsProduced++;

                    System.out.println(
                            "Producer "+Thread.currentThread().getName()+ " put rows A[" +
                                    item.getLowA() + "-" + item.getHighA() +
                                    "] and columns B[" +
                                    item.getLowB() + "-" + item.getHighB() + "] to buffer"
                    );
                    int sleep= rand.nextInt(maxProducerSleepTime);
                    threadSleepTime +=sleep;
                    Thread.sleep(sleep);
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
        long endTime= System.currentTimeMillis();
        executionTime=endTime- startTime;
    }
}