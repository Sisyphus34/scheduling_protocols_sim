/**
 * Name: Shawn Picardy
 * Course: cpsc 3220
 * Smotherman
 * March 17th. 
 */

import java.util.Scanner;
import java.util.Vector;

class Schedule {

    private static Vector<Integer> arrivalTime = new Vector<Integer>();
    private static Vector<Integer> serviceTime = new Vector<Integer>();

    private static int remainingTime; 
    private static int completionTime;
    private static int responseTime;
    private static int waitTime;  

    // int
    // task_id, /* alphabetic tid can be obtained as 'A'+(task_counter++) */
    // arrival_time,
    // service_time,
    // remaining_time,
    // completion_time,
    // response_time,
    // wait_time;

    public static void main(String[] args) {
        acquireTimes();
        printTraceTimes();
    }

    private static void acquireTimes() {
        // Create scanner for I/O redirection
        Scanner input = new Scanner(System.in);
        // Use a counter and modulus to retreive each value from stdin
        // then place every other value into respective vectors.
        int counter = 0;
        while (input.hasNext()) {
            String a = input.next();
            if ((counter % 2) == 0) {
                arrivalTime.add(Integer.parseInt(a));
            } else {
                serviceTime.add(Integer.parseInt(a));
            }
            counter++;
        }
        input.close(); // close scanner to prevent leak

        // convert both vectors to strings and print for debugging.
        arrivalTime.toString();
        serviceTime.toString();

        /** ARRIVAL TIMES */
        System.out.println("Arrival times are:");
        for (Integer element : arrivalTime) {
            System.out.print(element + " ");
        }
        System.out.print("\n\r");

        /** SERVICE TIMES */
        System.out.println("Service times are:");
        for (Integer element : serviceTime) {
            System.out.print(element + " ");
        }
        System.out.print("\n\r");

    }

    private static void printTraceTimes() {
        System.out.print("time   cpu   ready queue (tid/rst)\n----   ---   ---------------------\n\r");
    }

    private static void printTaskSummary() {
        System.out.print("Task summary table");
    }

    private static void printTimeSummary() {
        System.out.print("Time tracing");
    }
}
