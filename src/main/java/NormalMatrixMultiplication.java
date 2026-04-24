public class NormalMatrixMultiplication {
    public WorkItem matrixMultiplication(int[][]matrixA, int[][]matrixB){

        int[][] matrixC = new int[matrixA.length][matrixB[0].length]; //rowC= rowA, columnC=columnB
        long startTime= System.currentTimeMillis();
        for(int i=0; i<=matrixA.length-1; i++){
            for(int j=0;j<=matrixB[0].length-1; j++){
                for(int k=0; k<=matrixB.length-1; k++){
                    matrixC[i][j]+=matrixA[i][k]*matrixB[k][j];
                }
            }
        }
        long endTime=System.currentTimeMillis();
        return new WorkItem(matrixC, (endTime-startTime));
    }
}
