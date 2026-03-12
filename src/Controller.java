import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Controller {


public static void main(String[] args) throws IOException {
    AppConfig appConfig= LoadConfig.loadConfig(); //load config from the configuration file

    /*
    all the values from the configuration file
     */
    int m=appConfig.getM();
    int n=appConfig.getN();
    int p=appConfig.getP();
    int splitSize=appConfig.getSplitSize();
    int maxBuffSize=appConfig.getMaxBuffSize();

    /*
    create matrix
     */
    MatrixCreation matrixCreation= new MatrixCreation(appConfig, m, n, p);

    int[][] matrixA= matrixCreation.createMatrixA();
    int[][] matrixB= matrixCreation.createMatrixB();

    /*
    Multiply two matrices normally
     */
    NormalMatrixMultiplication normalMatrixMultiplication= new NormalMatrixMultiplication(matrixCreation);
    normalMatrixMultiplication.matrixMultiplcation(matrixA, matrixB, m, p);

    /*
    Split matrix into sub-matrix
    Requirements: size of matrix cannot be less than the splitSize: splitSize <= m && p
     */
    MatrixSplitter matrixSplitter= new MatrixSplitter(splitSize);
    matrixSplitter.splitBasedOnRow(0, matrixA);
    matrixSplitter.splitBasedOnColumn(0, matrixB,p);

    /*
    Multiply the matrix after breaking it into smaller subunits.
     */
    BlockingQueue<WorkItem> sharedBuffer = new ArrayBlockingQueue<>(maxBuffSize);

    //Create Producer and Consumer Threads
    Thread producerThread= new Thread(new Producer(sharedBuffer, matrixA, matrixB, p, splitSize));
    Thread consumerThread = new Thread(new Consumer(sharedBuffer, normalMatrixMultiplication));

    //start the threads
    producerThread.start();
    consumerThread.start();

}
}
