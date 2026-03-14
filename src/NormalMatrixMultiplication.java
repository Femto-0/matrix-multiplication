public class NormalMatrixMultiplication {

    public int [][] matrixMultiplication(int[][]matrixA, int[][]matrixB){
        int[][] matrixC = new int[matrixA.length][matrixB[0].length]; //rowC= rowA, columnC=columnB
        for(int i=0; i<=matrixA.length-1; i++){
            for(int j=0;j<=matrixB[0].length-1; j++){
                for(int k=0; k<=matrixB.length-1; k++){
                    matrixC[i][j]+=matrixA[i][k]*matrixB[k][j];
                }
            }
        }
        return matrixC;
    }
}
