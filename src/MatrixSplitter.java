public class MatrixSplitter {

    private final int splitSize;

    public MatrixSplitter(int splitSize){
        this.splitSize=splitSize;
    }

    public int[][] splitBasedOnRow(int currentRow, int[][]matrixToBeSplitted){
        int column= matrixToBeSplitted.length;
        int row = currentRow+splitSize;
        int[][] result= new int[row][column];
        System.out.println("Splitting matrix A for split size: "+ splitSize);
        for(int i=0; i<=row-1; i++) {
            for(int j = 0; j<=column -1; j++){
                result[i][j]=matrixToBeSplitted[i][j];
                System.out.print(result[i][j]+" ");
            }
            System.out.println();
        }
        return result;
    }

    /*
    unlike when splitting by rows, we don't have access to vertical width: number of rows. So, that's one extra input that the method needs.
     */
    public int[][] splitBasedOnColumn(int currentColumn,int[][]matrixToBeSplitted, int heightOfMatrix){
        int column= currentColumn+splitSize;
        int row= heightOfMatrix;
        int[][] result= new int[row][column];
        System.out.println("Splitting matrix B for split size: "+ splitSize);
        for(int i=0; i<=row-1; i++) {
            for(int j=0; j<=column-1; j++){
                result[i][j]=matrixToBeSplitted[i][j];
                System.out.print(result[i][j]+" ");
            }
            System.out.println();
        }
        return result;
    }
}
