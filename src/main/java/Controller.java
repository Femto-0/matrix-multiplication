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
    Logger logger= LoggerFactory.getLogger("logger.program");
    Logger conciseLogger = LoggerFactory.getLogger("logger.concise");

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
    conciseLogger.debug("-----------------START OF REPORT--------------------------");

    conciseLogger.debug("{Config:");
    conciseLogger.debug("   'm': {}", m);
    conciseLogger.debug("   'n': {}", n);
    conciseLogger.debug("   'p': {}", p);
    conciseLogger.debug("   'Max Buff Size': {}", maxBuffSize);
    conciseLogger.debug("   'Split Size': {}", splitSize);
    conciseLogger.debug("   'Number of Consumer': {}", numConsumer);
    conciseLogger.debug("   'Maximum Producer Sleep Time': {}", maxProducerSleepTime);
    conciseLogger.debug("   'Maximum Consumer Sleep Time': {}", maxConsumerSleepTime);
    conciseLogger.debug("Report: ");
    conciseLogger.debug("   Producer/ Consumer Simulation Result");
    conciseLogger.debug("   Simulation Time: {}ms", totalExecutionTime);
    conciseLogger.debug("   Maximum Thread Sleep Time: {}ms", totalThreadSleepTime);
    conciseLogger.debug("   Number of Producer Threads: 1");
    conciseLogger.debug("   Number of Consumer Threads: {}", numConsumer);
    conciseLogger.debug("   Size of Buffer: {} ", maxBuffSize);
    conciseLogger.debug("   Total number of Items produced: {} ", producer.getItemsProduced());
    conciseLogger.debug("   Thread 0: {}", producer.getItemsProduced());
    int totalNumberOfItems=0;
    int[] countPerThread= new int[numConsumer];
    for(int i=0; i<=numConsumer-1; i++){
        int count=consumerObjs[i].getItemsConsumed();
        totalNumberOfItems+=count;
        countPerThread[i]=count;
    }
    conciseLogger.debug("   Total number of Items consumed: {}", totalNumberOfItems);
    for(int i=0; i<=countPerThread.length-1; i++){
        conciseLogger.debug("   Thread {}: {}", (i+1), countPerThread[i]);
    }
    conciseLogger.debug("   Number of times Buffer was full: {} ", producer.getBufferFullCount());
    conciseLogger.debug("   Number of times Buffer was empty: {}",  totalBufferEmpty);
     /*
    Multiply two matrices normally
     */
    conciseLogger.debug("---------------------------------------------");
    WorkItem workItem= normalMatrixMultiplication.matrixMultiplication(matrixA, matrixB);
    long normalTotalTime=workItem.getTime();
    String matrixCNormal= Arrays.stream(workItem.getMatrix())
            .map(Arrays::toString)
            .collect(Collectors.joining("\n"));
    logger.debug("  Final matrix produced using for loops: {}{}", "\n", matrixCNormal);
    conciseLogger.debug("   Time taken to multiply using 'for' loops: {}ms", normalTotalTime);
    conciseLogger.debug("}");
    conciseLogger.debug("---------------------END OF REPORT------------------------");


}
}
