import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Lab3 {

    static class FileData {
        int k;
        List<List<Integer>> adjacencyLists;
    }

    public static void main(String[] args) {
        String filePath;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Unesite ime datoteke: ");
            filePath = "src/" + scanner.nextLine();
        }
        
        FileData fileData = new FileData();
        try {
            fileData = processFile(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Nepravilna datoteka: " + filePath);
            System.exit(1);
        } catch (Exception _) {
            System.out.println("Nepravilno formatirana datoteka: " + filePath);
            System.exit(1);
        }

        System.out.println("Rjesenje: " + (isKColorable(fileData.adjacencyLists, fileData.k) ? 1 : 0));
    }
    
    public static boolean isKColorable(List<List<Integer>> graph, int k) {
        return colorGraph(graph, new int[graph.size()], 0, k);
    }
    
    private static boolean colorGraph(List<List<Integer>> graph, int[] colors, int vertex, int k) {
        if (vertex >= graph.size()) { return true; }
        
        for (int color = 1; color <= k; color++) {
            int finalColor = color;
            if (graph.get(vertex).stream().noneMatch(v -> colors[v] == finalColor)) {
                colors[vertex] = color;
                if (colorGraph(graph, colors, vertex + 1, k)) { return true; }
                colors[vertex] = 0;
            }
        }
        return false;
    }

    private static FileData processFile(String filePath) throws FileNotFoundException {
        FileData fileData = new FileData();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int n = Integer.parseInt(scanner.nextLine());
            fileData.k = Integer.parseInt(scanner.nextLine());
            fileData.adjacencyLists = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                String line = scanner.nextLine();
                fileData.adjacencyLists.add(
                        IntStream.range(0, n)
                                .filter(j -> line.charAt(j) == '1')
                                .boxed()
                                .collect(Collectors.toCollection(ArrayList::new))
                );
            }
        }
        return fileData;
    }
}