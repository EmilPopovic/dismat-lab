import java.io.*;
import java.util.*;

public class Lab3 {

    static class FileData {
        int k;
        List<Vert> graph;
    }

    public static class Vert {
        int label;
        List<Vert> neighbors = new ArrayList<>();
        int color = 0;
        int degree = 0;

        Vert(int label) {
            this.label = label;
        }
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

        System.out.println("Rjesenje: " + (isKColorable(fileData.graph, fileData.k) ? 1 : 0));
    }

    public static boolean isKColorable(List<Vert> graph, int k) {
        if (graph.size() <= k) {
            return true;
        }
        
        graph.sort((v1, v2) -> v2.degree - v1.degree);

        boolean[] usedColors = new boolean[k + 1];

        return colorGraph(graph, 0, k, usedColors);
    }

    private static boolean colorGraph(List<Vert> graph, int index, int k, boolean[] usedColors) {
        if (index >= graph.size()) {
            return true;
        }

        Vert vertex = graph.get(index);
        
        Arrays.fill(usedColors, false);

        for (Vert neighbor : vertex.neighbors) {
            if (neighbor.color > 0) {
                usedColors[neighbor.color] = true;
            }
        }

        for (int color = 1; color <= k; color++) {
            if (!usedColors[color]) {
                vertex.color = color;
                if (colorGraph(graph, index + 1, k, usedColors)) {
                    return true;
                }
            }
        }

        vertex.color = 0;
        return false;
    }

    private static FileData processFile(String filePath) throws FileNotFoundException {
        FileData fileData = new FileData();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            int n = Integer.parseInt(scanner.nextLine());
            fileData.k = Integer.parseInt(scanner.nextLine());
            fileData.graph = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                fileData.graph.add(new Vert(i));
            }

            for (int i = 0; i < n; i++) {
                Vert v = fileData.graph.get(i);
                String line = scanner.nextLine();

                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '1') {
                        Vert u = fileData.graph.get(j);
                        v.neighbors.add(u);
                        v.degree++;
                    }
                }
            }
        }
        return fileData;
    }
}