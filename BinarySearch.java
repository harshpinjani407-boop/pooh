
import java.util.*;

public class BinarySearch {

    static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == key)
                return mid;

            else if (arr[mid] < key)
                low = mid + 1;

            else
                high = mid - 1;
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of elements: ");
        int n = sc.nextInt();

        int[] arr = new int[n];

        System.out.println("Enter elements:");
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();

        // Binary search requires sorted array
        Arrays.sort(arr);

        System.out.println("Sorted Array: " +
            Arrays.toString(arr));

        System.out.print("Enter element to search: ");
        int key = sc.nextInt();

        long start = System.nanoTime();

        int result = binarySearch(arr, key);

        long end = System.nanoTime();

        if (result == -1)
            System.out.println("Element not found");
        else
            System.out.println("Element found at index: " + result);

        System.out.println("Execution Time: " +
            (end - start) / 1000000.0 + " ms");

        System.out.println("Best Case: O(1)");
        System.out.println("Average Case: O(log n)");
        System.out.println("Worst Case: O(log n)");
        System.out.println("Space Complexity: O(1)");

        sc.close();
    }
}
