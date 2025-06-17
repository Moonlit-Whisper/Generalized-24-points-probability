/**
 * ClassName: GeneralizedTwentyFourGame
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author 不白之鸢
 * @Create 2025/6/17 15:54
 * @Version 1.0
 */
import java.util.Arrays;

public class GeneralizedTwentyFourGame {
    // 允许的目标值
    static final double TARGET = 24.0;
    // 误差容忍
    static final double EPS = 1e-6;
    // 点数范围
    static final int MIN = 1, MAX = 13;

    static boolean isZero(double x) {
        return Math.abs(x) < EPS;
    }

    // 通用递归（n 张牌）
    static boolean canReachTarget(double[] a, int n, double target) {
        if (n == 1) {
            return isZero(a[0] - target);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                // 剩下的数
                double[] next = new double[n - 1];
                int idx = 0;
                for (int k = 0; k < n; k++) {
                    if (k != i && k != j) next[idx++] = a[k];
                }
                // 枚举所有运算
                // 加
                next[n - 2] = a[i] + a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                // 乘
                next[n - 2] = a[i] * a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                // 减
                next[n - 2] = a[i] - a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[j] - a[i];
                if (canReachTarget(next, n - 1, target)) return true;
                // 除
                if (!isZero(a[j])) {
                    next[n - 2] = a[i] / a[j];
                    if (canReachTarget(next, n - 1, target)) return true;
                }
                if (!isZero(a[i])) {
                    next[n - 2] = a[j] / a[i];
                    if (canReachTarget(next, n - 1, target)) return true;
                }
            }
        }
        return false;
    }

    // 全部枚举（无重复，等价，顺序不同的情况合并。此实现为简单版，假设允许有重复，顺序不同视为同一组）
    static void enumerateAndCount(int n, double target) {
        int total = 0, can = 0;
        int[] arr = new int[n];

        // 枚举所有组合
        int[] nums = new int[n];
        enumerate(nums, 0, n, target, new int[n], MIN, MAX, new int[1], new int[1]);

        System.out.println("==== " + n + " 张牌 (" + MIN + "~" + MAX + ") ====");
        System.out.printf("组合总数：%d\n", totalCount(n, MAX - MIN + 1));
        System.out.printf("可以达标组合数：%d\n", countedTrue);
        System.out.printf("概率：%.5f\n\n", 1.0 * countedTrue / totalCount(n, MAX - MIN + 1));
        // countedTrue 由 enumerate() 静态变量记录
    }

    // 递归枚举所有 n 位，每位从 MIN 到 MAX（允许重复）
    static int countedTrue = 0;
    static void enumerate(int[] nums, int idx, int n, double target, int[] used, int min, int max, int[] total, int[] can) {
        if (idx == n) {
            double[] dnums = new double[n];
            for (int i = 0; i < n; i++) dnums[i] = nums[i];
            if (canReachTarget(dnums, n, target)) countedTrue++;
            return;
        }
        for (int v = min; v <= max; v++) {
            nums[idx] = v;
            enumerate(nums, idx + 1, n, target, used, min, max, total, can);
        }
    }

    // 计算排列总数（有放回排列），即 (MAX-MIN+1)^n
    static int totalCount(int n, int kind) {
        int total = 1;
        for (int i = 0; i < n; i++) total *= kind;
        return total;
    }

    public static void main(String[] args) {
        // 3张牌
        countedTrue = 0;
        enumerateAndCount(3, TARGET);
        // 4张牌
        countedTrue = 0;
        enumerateAndCount(4, TARGET);
        // 5张牌
        countedTrue = 0;
        enumerateAndCount(5, TARGET);
    }
}
