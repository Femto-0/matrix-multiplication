public class MatrixSplitter {

   public int[][] splitRows(int[][] originalMatrix, int currentRow, int splitSize){
       if(splitSize>= originalMatrix.length){
           System.out.println("split size is equal to size of original matrix. Returning original matrix");
           return originalMatrix;
       }
       else {
           if (currentRow >= originalMatrix.length - 1) {
               splitSize = 1;
           }
           int subRow = currentRow + splitSize;

           int[][] newMatrix = new int[subRow][originalMatrix[0].length];
           System.out.println("Matrix split by rows: ");
           for (int i = currentRow; i <= subRow - 1; i++) {
               for (int j = 0; j < originalMatrix[0].length; j++) {
                   newMatrix[i][j] = originalMatrix[i][j];
                   System.out.print(newMatrix[i][j] + " ");
               }
               System.out.println();
           }
           return newMatrix;
       }
   }
    public int[][] splitByColumns(int[][] originalMatrix, int currentColumn, int splitSize) {
       if(splitSize>= originalMatrix[0].length){
           System.out.println("split size is equal to size of original matrix. Returning original matrix");
           return originalMatrix;
       }
       else {
           if (currentColumn >= originalMatrix[0].length - 1) {
               splitSize = 1;
           }
           int subColumn = currentColumn + splitSize;

           int[][] newMatrix = new int[originalMatrix.length][subColumn];
           System.out.println("Matrix split by columns: ");
           for (int i = 0; i < originalMatrix.length; i++) {
               for (int j = currentColumn; j <= subColumn-1; j++) {
                   newMatrix[i][j] = originalMatrix[i][j];
                   System.out.print(newMatrix[i][j] + " ");
               }
               System.out.println();
           }
           return newMatrix;
       }
    }

    public static void main(String[] args) {
        int[][] test= {{1,2,  3, 4},{3,4,5, 6}, {5, 6, 7, 8}};
        int splitSize=2;
        MatrixSplitter matrixSplitter= new MatrixSplitter();
        int currentRow=test.length;
        for(int i=0; i<=currentRow-1;) {
            System.out.println("split: "+ i);
            matrixSplitter.splitRows(test, i, splitSize);
            i+=splitSize;
        }

        int currentColumn=test[0].length;
        for(int i=0; i<=currentColumn-1;) {
            System.out.println("split: "+ i);
            matrixSplitter.splitByColumns(test, i, splitSize);
            i+=splitSize;
        }

    }
}
