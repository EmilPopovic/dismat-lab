import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lab2 {

    static class FileData {
        int n;
        int k;
        int[][] matrix;

        FileData(int n, int k, int[][] matrix) {
            this.n = n;
            this.k = k;
            this.matrix = matrix;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Unesite ime datoteke: ");

        String filePath = "src/" + in.nextLine();

        int[][] matrix = null;
        int k = 0;

        try {
            FileData fileData = processFile(filePath);
            k = fileData.k;
            matrix = fileData.matrix;
        } catch (FileNotFoundException e) {
            System.out.println("Nepravilna datoteka: " + filePath);
            System.exit(1);
        }

        int cycleCount = countCyclesOfLength(matrix, k);

        System.out.println("Rjesenje: " + (cycleCount > 0 ? 1 : 0));

        in.close();
    }

    private static int countCyclesOfLength(int[][] adjMatrix, int cycleLength) {
        return trace(pow(adjMatrix, cycleLength));
    }

    private static int trace(int[][] matrix) {
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrica nije kvadratna.");
        }

        int tr = 0;
        for (int i = 0; i < matrix.length; i++) {
            tr += matrix[i][i];
        }
        return tr;
    }

    private static FileData processFile(String filePath) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(filePath));

        int n = Integer.parseInt(fileScanner.nextLine());
        int k = Integer.parseInt(fileScanner.nextLine());

        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            String line = fileScanner.nextLine();
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }

        fileScanner.close();

        return new FileData(n, k, matrix);
    }

    private static int[][] mul(int[][] a, int[][] b) {
        int rowsA = a.length;
        int rowsB = b.length;
        int colsA = a[0].length;
        int colsB = b[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Matrice nisu ulančane.");
        }

        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

    private static int[][] unitMatrix(int n) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        return result;
    }

    private static int[][] pow(int[][] mat, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("Negativan eksponent.");
        }

        if (mat.length != mat[0].length) {
            throw new IllegalArgumentException("Matrica nije kvadratna.");
        }

        int[][] result = unitMatrix(mat.length);
        int[][] base = mat;

        while (power > 0) {
            if (power % 2 == 1) {
                result = mul(result, base);
            }
            base = mul(base, base);
            power /= 2;
        }

        return result;
    }
}
