import java.util.ArrayList;

public class MatrixStream {
    static double sumParallelStream(ArrayList<ArrayList<Double>> matrix) {
        return matrix.parallelStream().map( (row) -> {
            double max = Double.MIN_VALUE;
            for (double elem : row) {
                if (elem > max) max = elem;
            }
            return max;
        } ).reduce(Double::sum).get();
    }
}