import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
    private final BlockingQueue<WorkItem> buffer;
    private static int[][] previousMatrix=new int[0][];
    WorkItem workItem= null;
    NormalMatrixMultiplication normalMatrixMultiplication;
    ConcatenateMatrix concatenateMatrix;

    public Consumer(BlockingQueue<WorkItem> buffer, NormalMatrixMultiplication normalMatrixMultiplication){
        this.buffer=buffer;
        this.normalMatrixMultiplication=normalMatrixMultiplication;
    }

    @Override
    public void run() {
        try {
            workItem= buffer.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
            int[][] subA= workItem.getSubA();
            int[][] subB= workItem.getSubB();
            int m= workItem.getLowA();
            int n= workItem.getHighA();
            int p= workItem.getHighB();
            boolean done=workItem.isDone();
            int [][] currentMatrix=normalMatrixMultiplication.matrixMultiplcation(subA, subB, m, p);
            previousMatrix=concatenateMatrix.concatenateMatrix(previousMatrix, currentMatrix);
            System.out.println("latest concatenated matrix: "+Arrays.deepToString(previousMatrix));
        }
}
