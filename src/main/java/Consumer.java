import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;

public class Consumer implements Runnable {

    private final BlockingQueue<WorkItem> buffer;
    private final NormalMatrixMultiplication multiplier;
    private final int[][] matrixC;
    private final int maxConsumerSleepTime;

    private AtomicInteger threadSleepTime = new AtomicInteger();
    private AtomicLong executionTime = new AtomicLong();

    private int itemsConsumed;
    private int bufferEmptyCount;

    private final Random rand = new Random();
    private final Logger logger;


    public Consumer(BlockingQueue<WorkItem> buffer, NormalMatrixMultiplication multiplier, int[][] matrixC, int maxConsumerSleepTime, Logger logger) {
        this.buffer = buffer;
        this.multiplier = multiplier;
        this.matrixC = matrixC;
        this.maxConsumerSleepTime = maxConsumerSleepTime;
        this.logger=logger;
    }

    public AtomicInteger getThreadSleepTime(){
        return threadSleepTime;
    }
    public AtomicLong getExecutionTime(){
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
                    logger.debug("....The queue is empty. Consumer {} is waiting...",
                            Thread.currentThread().getName());
                }

                WorkItem item = buffer.take();

                if (item.isDone()) {
                    logger.debug("Consumer {} exiting", Thread.currentThread().getName());
                    break;
                }
                WorkItem workItem=multiplier.matrixMultiplication(item.getSubA(), item.getSubB());
                int[][] subC = workItem.getMatrix();

                /*
                The output is out of order because of following three lines. need to work on this
                 */
                String subA=Arrays.stream(item.getSubA())
                        .map(Arrays::toString)
                        .collect(Collectors.joining("\n"));
                String subB=Arrays.stream(item.getSubB())
                        .map(Arrays::toString)
                        .collect(Collectors.joining("\n"));
                String subC_Calculation =  Arrays.stream(subC)
                        .map(Arrays::toString)
                        .collect(Collectors.joining("\n"));
                logger.debug("Consumer {} finished multiplying {}{} {}*{}{}{}and got result: {}{}",Thread.currentThread().getName(), "\n", subA,"\n","\n", subB,"\n", "\n" , subC_Calculation);

                //place subC into final matrix C
                for (int i = 0; i < subC.length; i++) {
                    System.arraycopy(subC[i], 0, matrixC[item.getLowA() + i], item.getLowB(), subC[0].length);
                }
                logger.debug("Consumer {} inserted calculated block into matrix C: " +
                                "Rows [{} - {}] and Cols [{} - {}]",
                                Thread.currentThread().getName(),
                                item.getLowA(), item.getHighA(),
                                item.getLowB(), item.getHighB());
                itemsConsumed++;
                int sleep= rand.nextInt(maxConsumerSleepTime);
                threadSleepTime.addAndGet(sleep);
                Thread.sleep(sleep);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTime= System.currentTimeMillis();
        executionTime.addAndGet(endTime-startTime);
    }

}