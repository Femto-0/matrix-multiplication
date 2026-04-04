public class PrintMatrix {

    int[][] matrix;
    String nameOfMatrix;

    public PrintMatrix(int[][] matrix, String nameOfMatrix){
        this.matrix=matrix;
        this.nameOfMatrix=nameOfMatrix;
    }

    public PrintMatrix(int [][] matrix){
        this.matrix=matrix;
    }
    public void printMatrix(){
        if(!nameOfMatrix.isEmpty()){
            System.out.println(nameOfMatrix+":");
        }
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }
    }
}
