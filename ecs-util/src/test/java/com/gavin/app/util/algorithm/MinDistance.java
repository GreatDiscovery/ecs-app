package com.gavin.app.util.algorithm;

/**
 * 给定n个点，求一个点到所有点的欧式距离最小，与真实值误差在 10^-5 之内的答案将被视作正确答案。
 *
 * @author gavin
 * @date 2020/7/12 10:08 下午
 */
public class MinDistance {
    public double getMinDistSum(int[][] positions) {
        double[] current_point = new double[2];
        for (int i = 0; i < positions.length; i++) {
            current_point[0] += positions[i][0];
            current_point[1] += positions[i][1];
        }
        current_point[0] /= positions.length;
        current_point[1] /= positions.length;
        double minimum_distance = distSum(current_point, positions, positions.length);
        int k = 0;
        while (k < positions.length) {
            for (int i = 0; i < positions.length && i != k; i++) {
                double[] newpoint = new double[2];
                newpoint[0] = positions[i][0];
                newpoint[1] = positions[i][1];
                double newd = distSum(newpoint, positions, positions.length);
                if (newd < minimum_distance) {
                    minimum_distance = newd;
                    current_point[0] = newpoint[0];
                    current_point[1] = newpoint[1];
                }
            }
            k++;
        }
        double test_distance = 1000;
        int flag = 0;
        double[][] test_point = { { -1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 0.0 }, { 0.0, -1.0 } };
        while (test_distance > 0.0001) {
            flag = 0;
            for (int i = 0; i < 4; i++) {
                double[] newpoint = new double[2];
                newpoint[0] = current_point[0] + (double) test_distance * test_point[i][0];
                newpoint[1] = current_point[1] + (double) test_distance * test_point[i][1];
                double newd = distSum(newpoint, positions, positions.length);
                if (newd < minimum_distance) {
                    minimum_distance = newd;
                    current_point[0] = newpoint[0];
                    current_point[1] = newpoint[1];
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                test_distance /= 2;
        }
        return minimum_distance;
    }

    double distSum(double[] p, int[][] arr, int n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double distx = Math.abs(arr[i][0] - p[0]);
            double disty = Math.abs(arr[i][1] - p[1]);
            sum += Math.sqrt((distx * distx) + (disty * disty));
        }

        return sum;
    }
}
