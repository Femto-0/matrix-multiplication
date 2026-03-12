import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{

    private final BlockingQueue<WorkItem> buffer;
    WorkItem workItem = new WorkItem();
    MatrixSplitter matrixSplitter;
    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int splitSize;
    private final int heightOfMatrixB;


    public Producer(BlockingQueue<WorkItem> buffer, int[][] matrixA, int[][] matrixB, int heightOfMatrixB, int splitSize){
        this.buffer= buffer;
        this.matrixA = matrixA;
        this.matrixB=matrixB;
        this.heightOfMatrixB = heightOfMatrixB;
        this.splitSize=splitSize;
    }


    @Override
    public void run() {
        matrixSplitter= new MatrixSplitter(splitSize);
        for(int i=0; i<matrixA.length;){
            workItem.setSubA(matrixSplitter.splitBasedOnRow(i, matrixA));
            workItem.setSubB(matrixSplitter.splitBasedOnColumn(i, matrixB, heightOfMatrixB));
            workItem.setLowA(i);
            workItem.setHighA((i+splitSize)-1);
            workItem.setLowB(i);
            workItem.setHighB((i+splitSize)-1);
            workItem.setDone(false);
            i+=splitSize;
            try {
                buffer.put(workItem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
