import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Lab2Test {

    @Test
    void hasKCycle() {
        // Path to the directory containing test files
        String testDataDir = "test_data/Lab2";

        // Get the list of test files
        File dir = new File(testDataDir);
        File[] testFiles = dir.listFiles((pathname) -> pathname.getName().endsWith(".txt"));

        if (testFiles == null) {
            throw new RuntimeException("No test files found in directory: " + testDataDir);
        }

        for (File testFile : testFiles) {
            try {
                // Process the test file
                TestFileData testData = processFile(testFile);

                // Run the test for each k in the specified interval
                for (int k = testData.minK; k <= testData.maxK; k++) {
                    boolean result = Lab2.hasKCycle(testData.matrix, k);
                    if (testData.expectedTrueValues.contains(k)) {
                        assertTrue(result, "Failed for k = " + k + " in file " + testFile.getName());
                    } else {
                        assertFalse(result, "Failed for k = " + k + " in file " + testFile.getName());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to process the test file and create TestFileData object
    private TestFileData processFile(File file) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(file);

        int n = Integer.parseInt(fileScanner.nextLine());
        fileScanner.nextLine(); // Skip the "k" line

        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            String line = fileScanner.nextLine();
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }

        // Read the min_k and max_k values
        String[] rangeLine = fileScanner.nextLine().split(" ");
        int minK = Integer.parseInt(rangeLine[0]);
        int maxK = Integer.parseInt(rangeLine[1]);

        // Read the expected true values for k
        String[] trueValuesLine = fileScanner.nextLine().split(" ");
        Set<Integer> expectedTrueValues = new HashSet<>();
        for (String value : trueValuesLine) {
            expectedTrueValues.add(Integer.parseInt(value));
        }

        fileScanner.close();

        return new TestFileData(matrix, minK, maxK, expectedTrueValues);
    }

    // Helper class to hold the test file data
    private static class TestFileData {
        int[][] matrix;
        int minK;
        int maxK;
        Set<Integer> expectedTrueValues;

        TestFileData(int[][] matrix, int minK, int maxK, Set<Integer> expectedTrueValues) {
            this.matrix = matrix;
            this.minK = minK;
            this.maxK = maxK;
            this.expectedTrueValues = expectedTrueValues;
        }
    }
}
