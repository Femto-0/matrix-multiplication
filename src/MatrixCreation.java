import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Random;
public class MatrixCreation {
    private int m;
    private int n;
    private int p;
    private int maxBuffSize;
    private int splitSize;
    private int numConsumer;
    private int maxProducerSleepTime;
    private int maxConsumerSleepTime;

    Random rand= new Random();

    public void getNumberOfRowsAndColumns() throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        ReadConfig readConfig= objectMapper.readValue(new File("src/config.json"), ReadConfig.class);

        this.m=readConfig.getM();
        this.n=readConfig.getN();
        this.p=readConfig.getP();
    }

    public int[][] createRowA() {
        int[][] rowA = new int[m][n];
        System.out.println("Matrix A");
        for (int i = 0; i <= m-1; i++) {
            for (int j = 0; j <=n-1; j++) {
                rowA[i][j] = rand.nextInt(10);
                System.out.print(rowA[i][j]+ " ");
            }
            System.out.println();
        }
        return rowA;
    }

    public int[][] createRowB(){
        int[][] rowB = new int[n][p];
        System.out.println("Matrix B: ");
        for (int i = 0; i <=n-1; i++) {
            for (int j = 0; j <=p-1; j++) {
                rowB[i][j] = rand.nextInt(10);
                System.out.print(rowB[i][j]+" ");
            }
            System.out.println();
        }
        return rowB;
    }

    public static void main(String[] args) throws IOException {
        MatrixCreation mc= new MatrixCreation();
        mc.getNumberOfRowsAndColumns();
        mc.createRowA();
        mc.createRowB();
    }
}
