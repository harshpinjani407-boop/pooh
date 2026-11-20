
import java.util.*;

public class FractionalKnapsack {

    static class Item {
        int id;
        double weight, profit, ratio;

        Item(int id, double weight, double profit) {
            this.id = id;
            this.weight = weight;
            this.profit = profit;
            this.ratio = profit / weight;
        }
    }

    static void knapsack(Item[] items, double capacity) {

        // Sort by profit/weight ratio (descending)
        Arrays.sort(items, (a, b) ->
            Double.compare(b.ratio, a.ratio));

        double totalProfit = 0;

        System.out.println("\nSelected Items:");

        for (Item item : items) {

            if (capacity == 0)
                break;

            if (item.weight <= capacity) {
                // Take complete item
                capacity -= item.weight;
                totalProfit += item.profit;

                System.out.println("Item " + item.id +
                    " -> 100% selected");
            }
            else {
                // Take fractional item
                double fraction = capacity / item.weight;

                totalProfit += item.profit * fraction;

                System.out.printf("Item %d -> %.2f%% selected%n",
                    item.id, fraction * 100);

                capacity = 0;
            }
        }

        System.out.printf("\nMaximum Profit: %.2f%n", totalProfit);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        Item[] items = new Item[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nItem " + (i + 1));

            System.out.print("Enter weight: ");
            double weight = sc.nextDouble();

            System.out.print("Enter profit: ");
            double profit = sc.nextDouble();

            if (weight <= 0 || profit < 0) {
                System.out.println("Invalid weight or profit");
                sc.close();
                return;
            }

            items[i] = new Item(i + 1, weight, profit);
        }

        System.out.print("\nEnter knapsack capacity: ");
        double capacity = sc.nextDouble();

        if (capacity < 0) {
            System.out.println("Invalid capacity");
            sc.close();
            return;
        }

        long start = System.nanoTime();

        knapsack(items, capacity);

        long end = System.nanoTime();

        System.out.println("Execution Time: " +
            (end - start) / 1000000.0 + " ms");

        System.out.println("Best Case: O(n log n)");
        System.out.println("Average Case: O(n log n)");
        System.out.println("Worst Case: O(n log n)");
        System.out.println("Space Complexity: O(n)");

        sc.close();
    }
}
