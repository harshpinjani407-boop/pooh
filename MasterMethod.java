
import java.util.*;

public class MasterMethod {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a: ");
        int a = sc.nextInt();

        System.out.print("Enter b: ");
        int b = sc.nextInt();

        System.out.print("Enter power of n (k): ");
        double k = sc.nextDouble();

        if (a < 1 || b <= 1 || k < 0) {
            System.out.println("Invalid input");
            return;
        }

        long start = System.nanoTime();

        double p = Math.log(a) / Math.log(b);

        System.out.println("\nRecurrence: T(n) = " +
                a + "T(n/" + b + ") + n^" + k);

        System.out.printf("log_b(a) = %.4f%n", p);

        double eps = 1e-9;

        if (k < p - eps) {
            System.out.println("Case 1");
            System.out.printf("Time Complexity: Theta(n^%.4f)%n", p);
        }
        else if (Math.abs(k - p) <= eps) {
            System.out.println("Case 2");
            System.out.printf(
                "Time Complexity: Theta(n^%.4f log n)%n", p);
        }
        else {
            System.out.println("Case 3");
            System.out.println("Time Complexity: Theta(n^" + k + ")");
        }

        long end = System.nanoTime();

        System.out.println("Analysis Execution Time: " +
                (end - start) / 1000000.0 + " ms");

        sc.close();
    }
}
