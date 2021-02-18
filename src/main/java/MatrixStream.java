import java.util.ArrayList;
import java.util.Comparator;

public class MatrixStream {
    public static void main(String[] args){
        double[][] matrixArray = {{1, 2, 3, 4.01, 5.01},
                {4, 5, 6, 13.01, 5.02},
                {7, 8, 9, 10.01, 19.98},
                {10, 11, 15, 1, 2},
                {13, 14, 11, 12, 15}
        };

        ArrayList<ArrayList<Double>> matrix = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < 5; j++) {
                matrix.get(i).add(matrixArray[i][j]);
            }
        }

        long startTime, finishTime;
        double result;
        for (int i = 1; i < 10; i++) {
            startTime = System.currentTimeMillis();
            result = sumParallelStream(matrix);
            finishTime = System.currentTimeMillis();
            System.out.printf("Parallelism level : %d\tTime required : %d\n", i, finishTime - startTime);
        }
    }

    static double sumParallelStream(ArrayList<ArrayList<Double>> matrix) {
        return matrix.parallelStream().map( (x) -> {
            double max = Double.MIN_VALUE;
            for (double elem : x) {
                if (elem > max) max = elem;
            }
            return max;
        }).reduce(Double::sum).get();
    }

    public static class DoubleComparator implements Comparator<Double> {

        @Override
        public int compare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }
    }
}