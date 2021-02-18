import org.junit.Assert;
import org.junit.jupiter.api.Test;

class MatrixParallelTest {

    @Test
    void maxRowSumTest() throws Exception {
        double[][] matrix = {{1, 2, 3, 4.01, 5.01},
                {4, 5, 6, 13.01, 5.02},
                {7, 8, 9, 10.01, 19.98},
                {10, 11, 15, 1, 2},
                {13, 14, 11, 12, 15}
        };
        double result, ANSWER1 = 68;

        for (int i = 1; i < 10; i++) {
            result = MatrixParallel.maxRowSum(i, matrix, 5);
            Assert.assertEquals(result, ANSWER1, 0);
        }

        matrix = new double[][]{ {1}, {2}, {3}, {4}, {5}, {6} };
        double ANSWER2 = 21;
        for (int i = 1; i < 10; i ++) {
            result = MatrixParallel.maxRowSum(i, matrix, 1);
            Assert.assertEquals(result, ANSWER2, 0);
        }
    }

    @Test
    void bigMaxRowSumTest() throws Exception {
        double[][] matrix = new double[10000][10000];
        double BIG_ANSWER = 0;

        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                matrix[i][j] = i * 10000 + j;
            }
            BIG_ANSWER += matrix[i][9999];
        }

        double result = MatrixParallel.maxRowSum(10, matrix, 10000);
        Assert.assertEquals(result, BIG_ANSWER, 0);
    }
}