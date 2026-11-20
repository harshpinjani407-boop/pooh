
import java.util.*;

public class LCS {

    static void lcs(String s1, String s2) {

        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        // Build DP Table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(
                        dp[i - 1][j],
                        dp[i][j - 1]
                    );
                }
            }
        }

        System.out.println("\nLCS Length: " + dp[m][n]);

        // Find actual LCS
        StringBuilder result = new StringBuilder();

        int i = m, j = n;

        while (i > 0 && j > 0) {

            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                result.append(s1.charAt(i - 1));
                i--;
                j--;
            }
            else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            }
            else {
                j--;
            }
        }

        System.out.println("LCS: " + result.reverse());

        // Display DP Table
        System.out.println("\nDP Table:");

        for (i = 0; i <= m; i++) {
            for (j = 0; j <= n; j++) {
                System.out.print(dp[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first string: ");
        String s1 = sc.next();

        System.out.print("Enter second string: ");
        String s2 = sc.next();

        long start = System.nanoTime();

        lcs(s1, s2);

        long end = System.nanoTime();

        System.out.println("\nExecution Time: " +
            (end - start) / 1000000.0 + " ms");

        System.out.println("Best Case: O(m * n)");
        System.out.println("Average Case: O(m * n)");
        System.out.println("Worst Case: O(m * n)");
        System.out.println("Space Complexity: O(m * n)");

        sc.close();
    }
}
