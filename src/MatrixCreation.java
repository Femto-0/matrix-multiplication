import java.util.Random;
public class MatrixCreation {

    AppConfig appConfig;
    private final int m; //number of rows in Matrix A
    private final int n; //number of columns in Matrix A or rows in Matrix B
    private final int p; //number of columns in Matrix B

    Random rand= new Random();

    public MatrixCreation(AppConfig appConfig, int m, int n, int p){
        this.appConfig=appConfig;
        this.m=m;
        this.n=n;
        this.p=p;
    }

    public int[][] createMatrixA() {
        int[][] matrixA = new int[m][n];
        System.out.println("Matrix A");
        for (int i = 0; i <= m-1; i++) {
            for (int j = 0; j <=n-1; j++) {
                matrixA[i][j] = rand.nextInt(10);
                System.out.print(matrixA[i][j]+ " ");
            }
            System.out.println();
        }
        return matrixA;
    }

    public int[][] createMatrixB(){
        int[][] matirxB = new int[n][p];
        System.out.println("Matrix B: ");
        for (int i = 0; i <=n-1; i++) {
            for (int j = 0; j <=p-1; j++) {
                matirxB[i][j] = rand.nextInt(10);
                System.out.print(matirxB[i][j]+" ");
            }
            System.out.println();
        }
        return matirxB;
    }

}
