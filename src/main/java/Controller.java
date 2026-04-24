import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.*;
public class Controller {


public static void main(String[] args) throws IOException, InterruptedException {
    AppConfig appConfig= LoadConfig.loadConfig(); //load config from the configuration file
    NormalMatrixMultiplication normalMatrixMultiplication= new NormalMatrixMultiplication();
    Logger logger= LoggerFactory.getLogger(Controller.class);

    /*
    all the values from the configuration file
     */
    int m=appConfig.getM();
    int n=appConfig.getN();
    int p=appConfig.getP();
    int splitSize=appConfig.getSplitSize();
    int maxBuffSize=appConfig.getMaxBuffSize();
    int numConsumer=appConfig.getNumConsumer();
    int maxProducerSleepTime=appConfig.getMaxProducerSleepTime();
    int maxConsumerSleepTime=appConfig.getMaxConsumerSleepTime();

    /*
    create matrix
     */
    MatrixCreation matrixCreation= new MatrixCreation(appConfig, m, n, p);
    int[][] matrixA= matrixCreation.createMatrixA();
    int[][] matrixB= matrixCreation.createMatrixB();

    String matrixA_String=Arrays.stream(matrixA)
            .map(Arrays::toString)
            .collect(Collectors.joining("\n"));
    logger.debug("Matrix A: {}{}", "\n", matrixA_String);

    String matrixB_String=Arrays.stream(matrixB)
            .map(Arrays::toString)
            .collect(Collectors.joining("\n"));
    logger.debug("Matrix B: {}{}", "\n", matrixB_String);

    int[][] matrixC= new int[m][p];

    /*
    Multiply the matrix via. Producer-Consumer model.
     */
    BlockingQueue<WorkItem> sharedBuffer = new ArrayBlockingQueue<>(maxBuffSize);

    Producer producer= new Producer(sharedBuffer, matrixA, matrixB, splitSize, numConsumer, maxProducerSleepTime, logger);
    Consumer[] consumerObjs = new Consumer[numConsumer];

    //Create Producer and Consumer Threads
    Thread producerThread= new Thread(producer);
    Thread[] consumers = new Thread[numConsumer];

    /*
    Start the Producer and Consumer Threads
     */

    producerThread.start(); //start the producer thread

    for(int i=0; i<numConsumer; i++){
        consumerObjs[i]= new Consumer(sharedBuffer, normalMatrixMultiplication, matrixC, maxConsumerSleepTime, logger);
        consumers[i]= new Thread(consumerObjs[i]);
        consumers[i].start(); //start the consumer thread
    }
    producerThread.join();   //shut down the producer thread
    for(Thread t: consumers){
        t.join();            //shut down the consumer thread
    }

    //calculating sleep time for Producer and Consumer Threads
    AtomicInteger producerSleepTime=producer.getThreadSleepTime();
    AtomicInteger consumerSleepTime= new AtomicInteger(0);

    //calculating the total execution time
    AtomicLong producerExecutionTime= producer.getExecutionTime();
    AtomicLong consumerExecutionTime= new AtomicLong(0);

    //buffer empty
    int totalBufferEmpty=0;


    for(Consumer consumer: consumerObjs){
        AtomicInteger currentThreadSleepTime= consumer.getThreadSleepTime();
        consumerSleepTime.getAndAdd(currentThreadSleepTime.get());

        AtomicLong currentExecutionTime= consumer.getExecutionTime();
        consumerExecutionTime.addAndGet(currentExecutionTime.get());

        totalBufferEmpty+=consumer.getBufferEmptyCount();
    }

    int finalConsumerSleepTime= consumerSleepTime.get();
    long finalConsumerExecutionTime= consumerExecutionTime.get();

    int totalThreadSleepTime= producerSleepTime.addAndGet(finalConsumerSleepTime);
    long totalExecutionTime= producerExecutionTime.addAndGet(finalConsumerExecutionTime);

    String matrixC_String=Arrays.stream(matrixC)
            .map(Arrays::toString)
            .collect(Collectors.joining("\n"));
    logger.debug("Final Matrix produced via. Multithreading: {}{}", "\n", matrixC_String);
    logger.debug("-------------------------------------------");
    logger.debug("| Producer/ Consumer Simulation Result");
    logger.debug("| Simulation Time: {}ms", totalExecutionTime);
    logger.debug("| Maximum Thread Sleep Time: {}ms", totalThreadSleepTime);
    logger.debug("| Number of Producer Threads: 1");
    logger.debug("| Number of Consumer Threads: {}", numConsumer);
    logger.debug("| Size of Buffer: {} ", maxBuffSize);
    logger.debug("| Total number of Items produced: {} ", producer.getItemsProduced());
    logger.debug("| Thread 0: {}", producer.getItemsProduced());
    int totalNumberOfItems=0;
    int[] countPerThread= new int[numConsumer];
    for(int i=0; i<=numConsumer-1; i++){
        int count=consumerObjs[i].getItemsConsumed();
        totalNumberOfItems+=count;
        countPerThread[i]=count;
    }
    logger.debug("| Total number of Items consumed: {}", totalNumberOfItems);
    for(int i=0; i<=countPerThread.length-1; i++){
        logger.debug("| Thread {}: {}", (i+1), countPerThread[i]);
    }
    logger.debug("| Number of times Buffer was full: {} ", producer.getBufferFullCount());
    logger.debug("| Number of times Buffer was empty: {}",  totalBufferEmpty);
    logger.debug("---------------------------------------------");
     /*
    Multiply two matrices normally
     */
    logger.debug("---------------------------------------------");
    WorkItem workItem= normalMatrixMultiplication.matrixMultiplication(matrixA, matrixB);
    long normalTotalTime=workItem.getTime();
    String matrixCNormal= Arrays.stream(workItem.getMatrix())
            .map(Arrays::toString)
            .collect(Collectors.joining("\n"));
    logger.debug("Final matrix produced using for loops: {}{}", "\n", matrixCNormal);
    logger.debug("Time taken: {}ms", normalTotalTime);
    logger.debug("---------------------------------------------");
}
}
