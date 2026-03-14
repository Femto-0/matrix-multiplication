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
    int[][] matrixCNormal= normalMatrixMultiplication.matrixMultiplication(matrixA, matrixB);

    /*
    Multiply the matrix after breaking it into smaller subunits.
     */
    BlockingQueue<WorkItem> sharedBuffer = new ArrayBlockingQueue<>(maxBuffSize);

    //Create Producer and Consumer Threads
    Thread producerThread= new Thread(new Producer(sharedBuffer, matrixA, matrixB, splitSize, numConsumer, maxProducerSleepTime));

    Thread[] consumers = new Thread[numConsumer];
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

    PrintMatrix pm= new PrintMatrix();
    System.out.println("Final Matrix C produced via Multithreading: ");
    pm.printMatrix(matrixC);

    System.out.println("Matrix C produced via 'for' loops ");
    pm.printMatrix(matrixCNormal);
}
}
