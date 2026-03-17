import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Controller {


public static void main(String[] args) throws IOException, InterruptedException {
    AppConfig appConfig= LoadConfig.loadConfig(); //load config from the configuration file

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
    Multiply two matrices normally
     */
    NormalMatrixMultiplication normalMatrixMultiplication= new NormalMatrixMultiplication();
    long normalStartTime= System.currentTimeMillis();
    int[][] matrixCNormal= normalMatrixMultiplication.matrixMultiplication(matrixA, matrixB);
    long normalStopTime= System.currentTimeMillis();
    long normalTotalTime=normalStopTime-normalStartTime;

    /*
    Multiply the matrix after breaking it into smaller subunits.
     */
    BlockingQueue<WorkItem> sharedBuffer = new ArrayBlockingQueue<>(maxBuffSize);

    //Create Producer and Consumer Threads
    Thread producerThread= new Thread(new Producer(sharedBuffer, matrixA, matrixB, splitSize, numConsumer, maxProducerSleepTime));

    Thread[] consumers = new Thread[numConsumer];

    long multiThreadingTimeStart= System.currentTimeMillis();
    //start the threads
    producerThread.start();

    for(int i=0; i<numConsumer; i++){
        consumers[i]= new Thread(new Consumer(sharedBuffer, normalMatrixMultiplication,matrixC,  maxConsumerSleepTime));
        consumers[i].start();
    }
    producerThread.join();
    for(Thread t: consumers){
        t.join();
    }
    long multiThreadingTimeFinish=System.currentTimeMillis();
    long multiThreadingTotalTime=multiThreadingTimeFinish-multiThreadingTimeStart;

    PrintMatrix pm= new PrintMatrix();
    System.out.println("Final Matrix C produced via Multithreading: ");
    pm.printMatrix(matrixC);
    System.out.println("Time taken: "+ multiThreadingTotalTime+"ms");
    System.out.println("---------------------------------------------------------------------");
    System.out.println("Matrix C produced via 'for' loops ");
    pm.printMatrix(matrixCNormal);
    System.out.println("Time taken: "+ normalTotalTime+"ms");
}
}
