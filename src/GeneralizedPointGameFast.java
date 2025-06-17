/**
 * ClassName: GeneralizedPointGameFast
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author 不白之鸢
 * @Create 2025/6/17 16:13
 * @Version 1.0
 */
import java.util.Arrays;
import java.util.HashSet;

public class GeneralizedPointGameFast {
    // 目标点数
    static final int[] TARGETS = {12, 10, 15, 18, 36, 60};
    // 张数
    static final int[] CARD_COUNTS = {2, 3, 4, 5};
    // 牌面范围
    static final int MIN = 1, MAX = 13;
    // 容忍误差
    static final double EPS = 1e-6;

    // 主递归判断
    static boolean canReachTarget(double[] a, int n, double target) {
        if (n == 1) {
            return Math.abs(a[0] - target) < EPS;
        }
        // 用hashset避免同一层重复状态
        HashSet<String> visited = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                double[] next = new double[n - 1];
                int idx = 0;
                for (int k = 0; k < n; k++) {
                    if (k != i && k != j) next[idx++] = a[k];
                }
                // 记录当前状态以避免重复分支
                String key = Arrays.toString(next) + "|" + a[i] + "," + a[j];
                if (visited.contains(key)) continue;
                visited.add(key);

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
                if (Math.abs(a[j]) > EPS) {
                    next[n - 2] = a[i] / a[j];
                    if (canReachTarget(next, n - 1, target)) return true;
                }
                if (Math.abs(a[i]) > EPS) {
                    next[n - 2] = a[j] / a[i];
                    if (canReachTarget(next, n - 1, target)) return true;
                }
            }
        }
        return false;
    }

    // 统计所有组合
    static void enumerateAndCount(int n, double target) {
        countedTrue = 0;
        int[] nums = new int[n];
        enumerate(nums, 0, n, target);
        int total = totalCount(n, MAX - MIN + 1);
        System.out.printf("牌数: %d, 目标: %.0f, 组合总数: %d, 可达: %d, 概率: %.5f\n",
                n, target, total, countedTrue, 1.0 * countedTrue / total);
    }

    // 递归枚举所有 n 位，每位从 MIN 到 MAX
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

    // 组合总数：(MAX-MIN+1)^n
    static int totalCount(int n, int kind) {
        int total = 1;
        for (int i = 0; i < n; i++) total *= kind;
        return total;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int n : CARD_COUNTS) {
            for (int t : TARGETS) {
                enumerateAndCount(n, t);
            }
        }
        System.out.println("总耗时: " + (System.currentTimeMillis() - start) + " ms");
    }
}