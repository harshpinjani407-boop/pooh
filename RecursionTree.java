
import java.util.*;

public class RecursionTree {

    static long recursionTree(int n, int level) {

        // Base case
        if (n <= 1)
            return 1;

        // Work at current node
        long work = (long) n * n;

        System.out.println("Level " + level +
                           " | n = " + n +
                           " | Work = " + work);

        // Three recursive calls
        long total = work;

        for (int i = 0; i < 3; i++)
            total += recursionTree(n / 4, level + 1);

        return total;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter n (power of 4): ");
        int n = sc.nextInt();

        if (n < 1) {
            System.out.println("Enter a positive number");
            return;
        }

        long start = System.nanoTime();

        long total = recursionTree(n, 0);

        long end = System.nanoTime();

        System.out.println("\nTotal Work: " + total);

        System.out.println("Execution Time: " +
            (end - start) / 1000000.0 + " ms");

        System.out.println("Time Complexity: O(n^2)");
        System.out.println("Space Complexity: O(log n)");

        sc.close();
    }
}
