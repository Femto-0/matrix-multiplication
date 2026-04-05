import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.slf4j.*;
public class Controller {


public static void main(String[] args) throws IOException, InterruptedException {
    AppConfig appConfig= LoadConfig.loadConfig(); //load config from the configuration file
    NormalMatrixMultiplication normalMatrixMultiplication= new NormalMatrixMultiplication();
    PrintMatrix printMatrix = new PrintMatrix();
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
    int producerSleepTime=producer.getThreadSleepTime();
    int consumerSleepTime = 0;

    //calculating the total execution time
    long producerExecutionTime= producer.getExecutionTime();
    long consumerExecutionTime=0;

    //buffer empty
    int totalBufferEmpty=0;


    for(Consumer consumer: consumerObjs){
        consumerSleepTime+=consumer.getThreadSleepTime();
        consumerExecutionTime+=consumer.getExecutionTime();
        totalBufferEmpty+=consumer.getBufferEmptyCount();
    }
    int maxThreadSleepTime=producerSleepTime+consumerSleepTime;
    long totalExecutionTime=producerExecutionTime+consumerExecutionTime;

    printMatrix.printMatrix(matrixC, "Final Matrix produced via. Multithreading");
    System.out.println("---------------------------------------------");
    System.out.println("| Producer/ Consumer Simulation Result");
    System.out.println("| Simulation Time: "+ totalExecutionTime+"ms");

    System.out.println("| Maximum Thread Sleep Time: "+ maxThreadSleepTime+ "ms");
    System.out.println("| Number of Producer Threads: 1");
    System.out.println("| Number of Consumer Threads: "+ numConsumer);
    System.out.println("| Size of Buffer: "+ maxBuffSize);
    System.out.println("| Total number of Items produced: "+ producer.getItemsProduced());
    System.out.println("| Thread 0: "+ producer.getItemsProduced());
    System.out.print("| Total number of Items consumed: ");
    int totalNumberOfItems=0;
    int[] countPerThread= new int[numConsumer];
    for(int i=0; i<=numConsumer-1; i++){
        int count=consumerObjs[i].getItemsConsumed();
        totalNumberOfItems+=count;
        countPerThread[i]=count;
    }
    System.out.println(totalNumberOfItems);
    for(int i=0; i<=countPerThread.length-1; i++){
        System.out.println("| Thread "+ (i+1)+": "+ countPerThread[i]);
    }
    System.out.println("| Number of times Buffer was full: "+ producer.getBufferFullCount());
    System.out.println("| Number of times Buffer was empty: "+ totalBufferEmpty);
    System.out.println("---------------------------------------------");
     /*
    Multiply two matrices normally
     */
    System.out.println("---------------------------------------------");
    WorkItem workItem= normalMatrixMultiplication.matrixMultiplication(matrixA, matrixB);
    int[][] matrixCNormal= workItem.getMatrix();
    long normalTotalTime=workItem.getTime();
    printMatrix.printMatrix(matrixCNormal, "Final matrix produced using for loops");
    System.out.println("Time taken: "+ normalTotalTime+"ms");
    System.out.println("---------------------------------------------");
}
}
