public class MatrixParallel {
    public static double maxRowSum(int threadsCount, double[][] matrix, int columns) throws Exception {
        double result = 0;
        if (threadsCount < 1) throw new Exception("Incorrect number of threads!");
        if (threadsCount > matrix.length) threadsCount = matrix.length;
        MatrixThread[] threads = new MatrixThread[threadsCount];

        int sliceSize = matrix.length / threadsCount, nextRow = 0;

        int i = 0;
        if (sliceSize * threadsCount < matrix.length) {
            int currSlice = sliceSize + 1;
            for (i = 0; i < matrix.length % threadsCount; i++) {
                threads[i] = new MatrixThread(i * currSlice, currSlice, matrix, columns);
                threads[i].start();
                nextRow = (i + 1) * currSlice;
            }
        }
        for (; i < threadsCount; i++) {
            threads[i] = new MatrixThread(nextRow, sliceSize, matrix, columns);
            threads[i].start();
            nextRow += sliceSize;
        }

        for (i = 0; i < threadsCount; i++) {
            threads[i].join();
            result += threads[i].getSum();
        }
        return result;
    }

    private static class MatrixThread extends Thread {
        private double sum = 0.0;
        private final int firstRow, sliceSize, columns;
        private final double[][] matrix;

        public double getSum() { return sum; }

        public MatrixThread(int firstRow, int sliceSize, double[][] matrix, int columns) {
            this.firstRow = firstRow;
            this.sliceSize = sliceSize;
            this.matrix = matrix;
            this.columns = columns;
        }

        @Override
        public void run() {
            for (int i = firstRow; i < firstRow + sliceSize; i++) {
                double rowMax = Double.MIN_VALUE;
                for (int j = 0; j < columns; j++) {
                    if (matrix[i][j] > rowMax) {
                        rowMax = matrix[i][j];
                    }
                }
                sum += rowMax;
            }
        }
    }

}