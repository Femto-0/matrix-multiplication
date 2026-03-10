import java.io.IOException;

public class NormalMatrixMultiplication {

    MatrixCreation matrixCreation;
    public NormalMatrixMultiplication(MatrixCreation matrixCreation){
        this.matrixCreation=matrixCreation;
    }

    public int [][] matrixMultiplcation(int[][]matrixA, int[][]matrixB, int rowC, int columnC){
        int[][] matrixC = new int[rowC][columnC]; //rowC= rowA, columnC=columnB
        System.out.println("Matrix C: ");
        for(int i=0; i<=rowC-1; i++){
            for(int j=0;j<=columnC-1; j++){
                for(int k=0; k<=matrixB.length-1; k++){
                    matrixC[i][j]+=matrixA[i][k]*matrixB[k][j];
                }
                System.out.print(matrixC[i][j]+" ");
            }
            System.out.println();
        }
        return matrixC;
    }

    public static void main(String[] args) throws IOException {
        AppConfig appConfig= LoadConfig.loadConfig();
        MatrixCreation matrixCreation= new MatrixCreation(appConfig,appConfig.getM(), appConfig.getN(), appConfig.getP());
        NormalMatrixMultiplication normalMatrixMultiplication= new NormalMatrixMultiplication(matrixCreation);
        normalMatrixMultiplication.matrixMultiplcation(matrixCreation.createMatrixA(), matrixCreation.createMatrixB(),appConfig.getM(), appConfig.getP());
    }
}
