
import java.util.*;

public class JobSequencing {

    static class Job {
        String id;
        int deadline, profit;

        Job(String id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }
    }

    static void scheduleJobs(Job[] jobs, int n) {

        Arrays.sort(jobs, (a, b) ->
            Integer.compare(b.profit, a.profit));

        int maxDeadline = 0;

        for (Job job : jobs)
            maxDeadline = Math.max(maxDeadline, job.deadline);

        String[] schedule = new String[maxDeadline + 1];
        boolean[] slotUsed = new boolean[maxDeadline + 1];

        int totalProfit = 0;

        for (int i = 0; i < n; i++) {
            for (int j = jobs[i].deadline; j >= 1; j--) {

                if (!slotUsed[j]) {
                    slotUsed[j] = true;
                    schedule[j] = jobs[i].id;
                    totalProfit += jobs[i].profit;
                    break;
                }
            }
        }

        System.out.println("\nScheduled Jobs:");

        for (int i = 1; i <= maxDeadline; i++) {
            if (slotUsed[i])
                System.out.print(schedule[i] + " ");
        }

        System.out.println("\nTotal Profit: " + totalProfit);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of jobs: ");
        int n = sc.nextInt();

        Job[] jobs = new Job[n];

        for (int i = 0; i < n; i++) {
            System.out.print("\nJob ID: ");
            String id = sc.next();

            System.out.print("Deadline: ");
            int deadline = sc.nextInt();

            System.out.print("Profit: ");
            int profit = sc.nextInt();

            jobs[i] = new Job(id, deadline, profit);
        }

        // Start execution time
        long start = System.nanoTime();

        scheduleJobs(jobs, n);

        // End execution time
        long end = System.nanoTime();

        double time = (end - start) / 1000000.0;

        System.out.println("\nExecution Time: " + time + " ms");

        System.out.println("Best Case: O(n log n + D)");
        System.out.println("Average Case: O(n log n + nD)");
        System.out.println("Worst Case: O(n log n + nD)");
        System.out.println("Space Complexity: O(n + D)");

        sc.close();
    }
}
