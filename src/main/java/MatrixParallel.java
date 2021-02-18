import java.util.concurrent.locks.ReentrantLock;

public class MatrixParallel {

    public static void main(String[] args){
        int a = 5;
        int b = 100;
    }

    public static double maxRowSum(int threadNum, double[][] matrix, int columns) throws Exception {
        double result;
        if (threadNum < 1) throw new Exception("Incorrect number of threads!");
        MatrixThread[] threads = new MatrixThread[threadNum];
        MatrixThread.resetSum();

        if (threadNum < matrix.length) {
            int sliceSize = (int) Math.round(matrix.length / (double) threadNum);

            int i = 0, rowsLeft = matrix.length;
            if (sliceSize == 1) {
                for (i = 0; i < matrix.length - threadNum; i++) {
                    threads[i] = new MatrixThread((i == 0) ? 0 : (i - 1) * 2 + 2, 2, matrix, columns);
                    threads[i].start();
                    rowsLeft -= 2;
                }
            } else {
                threads[i] = new MatrixThread(0, sliceSize, matrix, columns);
                threads[i++].start();
                rowsLeft -= sliceSize;
            }

            for (; i < threadNum; i++) {
                if (rowsLeft < sliceSize) {
                    threads[i] = new MatrixThread(
                            threads[i - 1].getFirstPosition() + threads[i - 1].getSliceSize(),
                            rowsLeft, matrix, columns);
                } else {
                    threads[i] = new MatrixThread(
                            threads[i - 1].getFirstPosition() + threads[i - 1].getSliceSize(),
                            sliceSize, matrix, columns);
                    rowsLeft -= sliceSize;
                }
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
        private final int firstPosition, sliceSize, columns;
        private final double[][] matrix;
        private static final  ReentrantLock locker = new ReentrantLock();

        public static void resetSum() {
            sum = 0.0;
        }

        public double getSum() {
            return sum;
        }
        public int getFirstPosition() { return firstPosition; }
        public int getSliceSize() { return sliceSize; }

        public MatrixThread(int firstPosition, int sliceSize, double[][] matrix, int columns) {
            this.firstPosition = firstPosition;
            this.sliceSize = sliceSize;
            this.matrix = matrix;
            this.columns = columns;
        }

        @Override
        public void run() {
            for (int i = firstPosition; i < firstPosition + sliceSize; i++) {
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