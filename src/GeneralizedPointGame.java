/**
 * ClassName: GeneralizedPointGame
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author 不白之鸢
 * @Create 2025/6/17 15:56
 * @Version 1.0
 */
import java.util.Arrays;

public class GeneralizedPointGame {
    // 可配置目标点数
    static final int[] TARGETS = {12, 10, 15, 18, 36, 60};
    static final double EPS = 1e-6;
    static final int MIN = 1, MAX = 13;

    static boolean isZero(double x) {
        return Math.abs(x) < EPS;
    }

    // 通用递归（n 张牌），判断能否算出 target
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
                next[n - 2] = a[i] + a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[i] * a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[i] - a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[j] - a[i];
                if (canReachTarget(next, n - 1, target)) return true;
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

    // 统计所有情况
    static void enumerateAndCount(int n, double target) {
        countedTrue = 0;
        int[] nums = new int[n];
        enumerate(nums, 0, n, target);
        int total = totalCount(n, MAX - MIN + 1);
        System.out.printf("目标点数: %.0f\n", target);
        System.out.printf("组合总数: %d\n", total);
        System.out.printf("可达组合数: %d\n", countedTrue);
        System.out.printf("概率: %.5f\n\n", 1.0 * countedTrue / total);
    }

    // 递归枚举所有 n 位，每位从 MIN 到 MAX（允许重复）
    static int countedTrue = 0;
    static void enumerate(int[] nums, int idx, int n, double target) {
        if (idx == n) {
            double[] dnums = new double[n];
            for (int i = 0; i < n; i++) dnums[i] = nums[i];
            if (canReachTarget(dnums, n, target)) countedTrue++;
            return;
        }
        for (int v = MIN; v <= MAX; v++) {
            nums[idx] = v;
            enumerate(nums, idx + 1, n, target);
        }
    }

    // (MAX-MIN+1)^n
    static int totalCount(int n, int kind) {
        int total = 1;
        for (int i = 0; i < n; i++) total *= kind;
        return total;
    }

    public static void main(String[] args) {
        int n = 4; // 4张牌
        for (int t : TARGETS) {
            enumerateAndCount(n, t);
        }
    }
}
