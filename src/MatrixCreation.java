import java.util.Scanner;
import java.util.Random;
public class MatrixCreation {

    Random rand= new Random();

    public int[] getNumberOfRowsAndColumns(){
        int[] numberOfRowsAndColumn= new int[3];
        Scanner sc= new Scanner(System.in);
        System.out.println("""
            Enter the number of rows and columns for A and B
            The format to enter the rows and columns are as follows:
            First enter number of rows for A
            Then enter the number of columns for A which is also going to be number of rows for B
            Finally, enter the number of Columns for B
            """);
        System.out.print("Enter the number of rows for A: ");
        numberOfRowsAndColumn[0]= sc.nextInt();
        System.out.print("Enter the number of columns for A/ rows for B: ");
        numberOfRowsAndColumn[1]= sc.nextInt();
        System.out.print("Enter the number of columns for B: ");
        numberOfRowsAndColumn[2]= sc.nextInt();

        return numberOfRowsAndColumn;
    }

    public int[][] createRowA(int row, int column) {
        int[][] rowA = new int[row][column];
        System.out.println("Matrix A");
        for (int i = 0; i <= row-1; i++) {
            for (int j = 0; j <=column-1; j++) {
                rowA[i][j] = rand.nextInt(10);
                System.out.print(rowA[i][j]+ " ");
            }
            System.out.println();
        }
        return rowA;
    }

    public int[][] createRowB(int row, int column){
        int[][] rowB = new int[row][column];
        System.out.println("Matrix B: ");
        for (int i = 0; i <=row-1; i++) {
            for (int j = 0; j <=column-1; j++) {
                rowB[i][j] = rand.nextInt(10);
                System.out.print(rowB[i][j]+" ");
            }
            System.out.println();
        }
        return rowB;
    }

    public static void main(String[] args) {
        MatrixCreation mc= new MatrixCreation();
        int[] numberOfRowsAndColumn=mc.getNumberOfRowsAndColumns();
        int rowA= numberOfRowsAndColumn[0];
        int columnA_RowB =numberOfRowsAndColumn[1];
        int columnB= numberOfRowsAndColumn[2];
        mc.createRowA(rowA,columnA_RowB);
        mc.createRowB(columnA_RowB, columnB);
    }
}
