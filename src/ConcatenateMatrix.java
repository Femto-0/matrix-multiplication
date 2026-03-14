public class ConcatenateMatrix {

    public int[][] concatenateMatrix(int[][]firstMatrix, int[][]secondMatrix){
        ///
        // 1. Create a new result matrix with combined total rows
        int newTotalRows = firstMatrix.length + secondMatrix.length;
        int[][] result = new int[newTotalRows][];

        // 2. Copy all rows from the first matrix into the new matrix
        System.arraycopy(firstMatrix, 0, result, 0, firstMatrix.length);
        // Source array, source start index, destination array, destination start index, length to copy

        // 3. Copy all rows from the second matrix into the new matrix, starting after the first matrix's rows
        System.arraycopy(secondMatrix, 0, result, firstMatrix.length, secondMatrix.length);

        return result;
    }
}
