import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lab2 {

    static class FileData {
        int k;
        int[][] matrix;

        FileData(int k, int[][] matrix) {
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
        } catch (Exception _) {
            System.out.println("Nepravilan format datoteke: " + filePath);
            System.exit(1);
        }

        System.out.println("Rjesenje: " + hasKCycle(matrix, k));

        in.close();
    }

    public static boolean hasKCycle(int[][] graph, int k) {
        if (k < 3 || k > graph.length) {
            return false;
        }

        boolean[] visited = new boolean[graph.length];

        // za svaki vrh provjeri je li dio ciklusa trazene duljine
        for (int i = 0; i < graph.length; i++) {
            if (dfs(graph, visited, i, i, 0, k)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(int[][] graph, boolean[] visited, int current, int start, int depth, int k) {
        if (depth == k) {
            return current == start;
        }

        visited[current] = true;

        for (int i = 0; i < graph.length; i++) {
            if (graph[current][i] == 0) {
                continue;
            }

            if (!visited[i]) {
                if (dfs(graph, visited, i, start, depth + 1, k)) {
                    return true;
                }
            } else if (i == start && depth + 1 == k) {
                return true;
            }
        }

        visited[current] = false;
        return false;
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

        return new FileData(k, matrix);
    }
}
