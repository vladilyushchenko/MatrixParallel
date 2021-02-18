import java.util.ArrayList;
import java.util.Random;

public class ParallelTesting {

    public static void main(String[] args) throws Exception {
        int threadsCount = 10, rowsCount = 10000, colsCount = 10000;
        long[] timeResults = testParallelThreads(threadsCount, rowsCount, rowsCount);

        System.out.println("Testing of threads implementation...");
        for (int i = 0; i < threadsCount; i++) {
            System.out.printf("Number of threads : %d, taken time : %d%n", i + 1, timeResults[i]);
        }
        System.out.printf("\n---------------------------------\nTesting of streams implementation...\nTaken " +
                "time : %d", testParallelStream(rowsCount, colsCount));
    }

    public static long[] testParallelThreads(int threadsCount, int rowsCount, int colsCount) throws Exception {
        double[][] matrix = randomMatrix(rowsCount, colsCount);
        long[] timeResults = new long[threadsCount];
        long startTime, finishTime;

        for (int i = 1; i <= threadsCount; i++) {
            startTime = System.currentTimeMillis();
            MatrixParallel.maxRowSum(i, matrix, colsCount);
            finishTime = System.currentTimeMillis();
            timeResults[i - 1] = finishTime  - startTime;
        }

        return timeResults;
    }

    public static long testParallelStream(int rowsCount, int colsCount) {
        ArrayList<ArrayList<Double>> arrayMatrix = randomArrayMatrix(rowsCount, colsCount);
        long startTime, finishTime;

        startTime = System.currentTimeMillis();
        double result = MatrixStream.sumParallelStream(arrayMatrix);
        finishTime = System.currentTimeMillis();

        return finishTime - startTime;
    }


    public static double[][] randomMatrix(int n, int m) {
        double[][] matrix = new double[n][m];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = rand.nextDouble();
            }
        }
        return matrix;
    }

    public static ArrayList<ArrayList<Double>> randomArrayMatrix(int n, int m) {
        ArrayList<ArrayList<Double>> arrayMatrix = new ArrayList<>(n);
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            arrayMatrix.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                arrayMatrix.get(i).add(rand.nextDouble());
            }
        }
        return arrayMatrix;
    }
}