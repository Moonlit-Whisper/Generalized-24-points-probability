/**
 * ClassName: GeneralizedPointGameInteractive
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author 不白之鸢
 * @Create 2025/6/17 16:18
 * @Version 1.0
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class GeneralizedPointGameInteractive {
    static final int MIN_CARD = 1, MAX_CARD = 13;
    static final double EPS = 1e-6;

    // 判断能否算出 target
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
                String key = Arrays.toString(next) + "|" + a[i] + "," + a[j];
                if (visited.contains(key)) continue;
                visited.add(key);

                next[n - 2] = a[i] + a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[i] * a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[i] - a[j];
                if (canReachTarget(next, n - 1, target)) return true;
                next[n - 2] = a[j] - a[i];
                if (canReachTarget(next, n - 1, target)) return true;
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

    static int countedTrue = 0;

    static void enumerate(int[] nums, int idx, int n, double target) {
        if (idx == n) {
            double[] dnums = new double[n];
            for (int i = 0; i < n; i++) dnums[i] = nums[i];
            if (canReachTarget(dnums, n, target)) countedTrue++;
            return;
        }
        for (int v = MIN_CARD; v <= MAX_CARD; v++) {
            nums[idx] = v;
            enumerate(nums, idx + 1, n, target);
        }
    }

    static int totalCount(int n, int kind) {
        int total = 1;
        for (int i = 0; i < n; i++) total *= kind;
        return total;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("广义算点游戏");
        System.out.println("请输入目标点数 (1~60)：");
        int target = -1;
        while (target < 1 || target > 60) {
            System.out.print("目标点数 = ");
            target = sc.nextInt();
            if (target < 1 || target > 60) {
                System.out.println("请输入1到60之间的整数！");
            }
        }
        int n = 0;
        System.out.println("请输入牌数 (2~5)：");
        while (n < 2 || n > 5) {
            System.out.print("牌数 = ");
            n = sc.nextInt();
            if (n < 2 || n > 5) {
                System.out.println("请输入2~5之间的整数！");
            }
        }
        countedTrue = 0;
        long start = System.currentTimeMillis();
        enumerate(new int[n], 0, n, target);
        int total = totalCount(n, MAX_CARD - MIN_CARD + 1);
        System.out.printf("牌数: %d, 目标: %d, 组合总数: %d, 可达: %d, 概率: %.5f\n",
                n, target, total, countedTrue, 1.0 * countedTrue / total);
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + " ms");
    }
}
