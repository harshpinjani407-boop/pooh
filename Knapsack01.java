
import java.util.*;

public class Knapsack01 {

    static void knapsack(int[] wt, int[] profit, int n, int W) {

        int[][] dp = new int[n + 1][W + 1];

        // Build DP Table
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {

                if (wt[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        profit[i - 1] + dp[i - 1][w - wt[i - 1]],
                        dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        System.out.println("\nMaximum Profit: " + dp[n][W]);

        // Display selected items
        System.out.println("Selected Items:");
        int w = W;

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                System.out.println("Item " + i +
                    " (Weight: " + wt[i - 1] +
                    ", Profit: " + profit[i - 1] + ")");

                w -= wt[i - 1];
            }
        }

        // Display DP Table
        System.out.println("\nDP Table:");
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= W; j++)
                System.out.print(dp[i][j] + "\t");

            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        int[] wt = new int[n];
        int[] profit = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nItem " + (i + 1));

            System.out.print("Enter weight: ");
            wt[i] = sc.nextInt();

            System.out.print("Enter profit: ");
            profit[i] = sc.nextInt();
        }

        System.out.print("\nEnter knapsack capacity: ");
        int W = sc.nextInt();

        long start = System.nanoTime();

        knapsack(wt, profit, n, W);

        long end = System.nanoTime();

        System.out.println("\nExecution Time: " +
            (end - start) / 1000000.0 + " ms");

        System.out.println("Best Case: O(n * W)");
        System.out.println("Average Case: O(n * W)");
        System.out.println("Worst Case: O(n * W)");
        System.out.println("Space Complexity: O(n * W)");

        sc.close();
    }
}
