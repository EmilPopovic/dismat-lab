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
        System.out.print("Unesite ime datoteke: ");
        String path;
        try (Scanner scanner = new Scanner(System.in)) {
            path = "src/" + scanner.nextLine();
        }

        FileData data = new FileData();
        try {
            data = processFile(path);
        } catch (FileNotFoundException e) {
            System.out.println("Nepravilna datoteka: " + path);
            System.exit(1);
        } catch (Exception _) {
            System.out.println("Nepravilno formatirana datoteka: " + path);
            System.exit(1);
        }

        System.out.println("Rjesenje: " + (isKColorable(data.graph, data.k) ? 1 : 0));
    }

    public static boolean isKColorable(List<Vert> graph, int k) {
        if (graph.size() <= k)
            return true;
        
        graph.sort((v, w) -> w.degree - v.degree);

        boolean[] usedColors = new boolean[k + 1];

        return colorGraph(graph, 0, k, usedColors);
    }

    private static boolean colorGraph(List<Vert> graph, int i, int k, boolean[] usedColors) {
        if (i >= graph.size())
            return true;

        Vert v = graph.get(i);
        
        Arrays.fill(usedColors, false);

        for (Vert neighbor : v.neighbors)
            if (neighbor.color > 0)
                usedColors[neighbor.color] = true;

        for (int color = 1; color <= k; color++) {
            if (!usedColors[color]) {
                v.color = color;
                if (colorGraph(graph, i + 1, k, usedColors))
                    return true;
            }
        }

        v.color = 0;
        return false;
    }

    private static FileData processFile(String filePath) throws FileNotFoundException {
        FileData data = new FileData();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            int n = Integer.parseInt(scanner.nextLine());
            data.k = Integer.parseInt(scanner.nextLine());
            data.graph = new ArrayList<>(n);

            for (int i = 0; i < n; i++)
                data.graph.add(new Vert(i));

            for (int i = 0; i < n; i++) {
                Vert v = data.graph.get(i);
                String line = scanner.nextLine();

                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '1') {
                        Vert u = data.graph.get(j);
                        v.neighbors.add(u);
                        v.degree++;
                    }
                }
            }
        }
        return data;
    }
}