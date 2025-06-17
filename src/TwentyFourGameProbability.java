import java.util.Arrays;

/**
 * ClassName: TwentyFourGameProbability
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author 不白之鸢
 * @Create 2025/6/17 15:51
 * @Version 1.0
 */
import java.util.Arrays;

public class TwentyFourGameProbability {

    // 判断离0的距离是否足够小
    static boolean isZero(double x) {
        return Math.abs(x) <= 1e-6;
    }

    // 递归算24点
    static boolean count24(double[] a, int n) {
        // 判断结果是否为24(递归跳出条件)
        if (n == 1) {
            return isZero(a[0] - 24);
        } else {
            for (int i = 0; i < n - 1; i++) { // 取第一个数
                for (int j = i + 1; j < n; j++) { // 取第二个数
                    double[] temp = new double[n - 1];
                    int iTemp = 0;
                    // 将其他数放到新数组
                    for (int k = 0; k < n; k++) {
                        if (k != i && k != j) {
                            temp[iTemp++] = a[k];
                        }
                    }
                    // 加法计算
                    temp[iTemp] = a[i] + a[j];
                    if (count24(temp, n - 1)) return true;
                    // 减法计算(无交换律)
                    temp[iTemp] = a[i] - a[j];
                    if (count24(temp, n - 1)) return true;
                    temp[iTemp] = a[j] - a[i];
                    if (count24(temp, n - 1)) return true;
                    // 乘法计算
                    temp[iTemp] = a[i] * a[j];
                    if (count24(temp, n - 1)) return true;
                    // 除法计算(无交换律)
                    if (!isZero(a[j])) {
                        temp[iTemp] = a[i] / a[j];
                        if (count24(temp, n - 1)) return true;
                    }
                    if (!isZero(a[i])) {
                        temp[iTemp] = a[j] / a[i];
                        if (count24(temp, n - 1)) return true;
                    }
                }
            }
            return false;
        }
    }

    // 两两组合算24点，例如(a+b)*(c+d)
    static boolean count24_(double[] a, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                // 取数
                double[] temp = new double[2];
                int iTemp = 0;
                for (int k = 0; k < n; k++) {
                    if (k != i && k != j) {
                        temp[iTemp++] = a[k];
                    }
                }
                // 计算组合所有运算
                double[] one = {
                        a[i] + a[j],
                        a[i] - a[j],
                        a[j] - a[i],
                        a[i] * a[j],
                        (isZero(a[j]) ? Double.NaN : a[i] / a[j]),
                        (isZero(a[i]) ? Double.NaN : a[j] / a[i])
                };
                double[] two = {
                        temp[0] + temp[1],
                        temp[0] - temp[1],
                        temp[1] - temp[0],
                        temp[0] * temp[1],
                        (isZero(temp[1]) ? Double.NaN : temp[0] / temp[1]),
                        (isZero(temp[0]) ? Double.NaN : temp[1] / temp[0])
                };

                // 判断结果
                for (int m = 0; m < 6; m++) {
                    for (int n1 = 0; n1 < 6; n1++) {
                        double o = one[m], t = two[n1];
                        if (Double.isNaN(o) || Double.isNaN(t)) continue;
                        if (isZero(o + t - 24)) return true;
                        if (isZero(o - t - 24)) return true;
                        if (isZero(t - o - 24)) return true;
                        if (isZero(o * t - 24)) return true;
                        if (!isZero(t) && isZero(o / t - 24)) return true;
                        if (!isZero(o) && isZero(t / o - 24)) return true;
                    }
                }
            }
        }
        return false;
    }

    // 计算概率
    public static void main(String[] args) {
        double[] a = new double[4];
        double truenumber = 0; // 可算24点的组合数
        double falsenumber = 0;// 不可算24点的组合数

        // 1. 四张牌分成一堆。一种分法
        for (int i = 1; i <= 13; i++) {
            // 4 计算1次
            Arrays.fill(a, i);
            if (count24(a, 4) || count24_(a, 4))
                truenumber += 1;
            else
                falsenumber += 1;
        }

        // 2. 四张牌分成两堆，两种分法
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 13; j++) {
                if (i != j) {
                    // 3,1 计算16次
                    a[0] = a[1] = a[2] = i;
                    a[3] = j;
                    if (count24(a, 4) || count24_(a, 4))
                        truenumber += 16;
                    else
                        falsenumber += 16;
                }
                if (i < j) {
                    // 2,2 计算36次
                    a[0] = a[1] = i;
                    a[2] = a[3] = j;
                    if (count24(a, 4) || count24_(a, 4))
                        truenumber += 36;
                    else
                        falsenumber += 36;
                }
            }
        }

        // 3. 四张牌分成三堆，一种分法
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 12; j++) {
                for (int m = j + 1; m <= 13; m++) {
                    if (i != j && i != m) {
                        // 2,1,1 计算96次
                        a[0] = a[1] = i;
                        a[2] = j;
                        a[3] = m;
                        if (count24(a, 4) || count24_(a, 4))
                            truenumber += 96;
                        else
                            falsenumber += 96;
                    }
                }
            }
        }

        // 4. 四张牌分成四堆，一种分法
        for (int i = 1; i <= 10; i++) {
            for (int j = i + 1; j <= 11; j++) {
                for (int m = j + 1; m <= 12; m++) {
                    for (int n = m + 1; n <= 13; n++) {
                        // 1,1,1,1 计算256次
                        a[0] = i;
                        a[1] = j;
                        a[2] = m;
                        a[3] = n;
                        if (count24(a, 4) || count24_(a, 4))
                            truenumber += 256;
                        else
                            falsenumber += 256;
                    }
                }
            }
        }

        double p = truenumber / (truenumber + falsenumber); // 计算概率
        System.out.printf("可算24点%.0f 不可算24点%.0f\n", truenumber, falsenumber);
        System.out.printf("概率值为%.5f\n", p); // 输出结果
    }
}
