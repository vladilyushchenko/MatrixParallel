import java.util.concurrent.locks.ReentrantLock;

public class MatrixParallel {

    public static double maxRowSum(int threadNum, double[][] matrix, int columns) throws Exception {
        double result;
        if (threadNum < 1) throw new Exception("Incorrect number of threads!");
        MatrixThread[] threads = new MatrixThread[threadNum];
        MatrixThread.resetSum();

        if (threadNum < matrix.length) {
            int sliceSize = matrix.length / threadNum;

            int i = 0;
            if (sliceSize * threadNum < matrix.length) {
                int currSlice =  sliceSize + 1;
                for (i = 0; i < matrix.length % threadNum; i++) {
                    threads[i] = new MatrixThread(i * currSlice, currSlice, matrix, columns);
                    threads[i].start();
                }
            } else {
                threads[i] = new MatrixThread(0, sliceSize, matrix, columns);
                threads[i++].start();
            }

            for (; i < threadNum; i++) {
                threads[i] = new MatrixThread(threads[i - 1].getFirstRow() + threads[i - 1].getSliceSize(),
                        sliceSize, matrix, columns);
                threads[i].start();
            }
        } else {
            threadNum = matrix.length;
            for (int i = 0; i < matrix.length; i++) {
                threads[i] = new MatrixThread(i, 1, matrix, columns);
                threads[i].start();
            }
        }

        for (int i = 0; i < threadNum; i++) {
            threads[i].join();
        }

        result = threads[0].getSum();
        return result;
    }

    private static class MatrixThread extends Thread {
        private static double sum = 0.0;
        private final int firstRow, sliceSize, columns;
        private final double[][] matrix;
        private static final ReentrantLock locker = new ReentrantLock();

        public static void resetSum() {
            sum = 0.0;
        }

        public double getSum() { return sum; }
        public int getFirstRow() { return firstRow; }
        public int getSliceSize() { return sliceSize; }

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
                synchronized (locker) {
                    sum += rowMax;
                }
            }
        }
    }
}