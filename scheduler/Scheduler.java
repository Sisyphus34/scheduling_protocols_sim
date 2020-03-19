
/**
 * Name: Shawn Picardy
 * Course: cpsc 3220
 * Smotherman
 * March 17th. 
 */

package scheduler;

import java.util.Scanner;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Queue;

class Scheduler {

    private static Vector<Integer> arrivalTime = new Vector<Integer>();
    private static Vector<Integer> serviceTime = new Vector<Integer>();
    private static Vector<Character> taskID = new Vector<Character>();
    private static Queue<Task> taskQueue = new LinkedList<Task>();
    private static Queue<Task> readyQueue = new LinkedList<Task>();
    private static Vector<Task> completedTasks = new Vector<Task>();

    private static char tid;
    private static int at;
    private static int st;
    private static int time = 0;
    private static int taskStart;
    private static boolean endTask = false;
    private static String emptyQ = "--";
    private static boolean taskRunning = false;

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
        buildTaskList();
        callFifo();
        printTaskSummary();
        printTimeSummary();
    }

    private static void callFifo() {

        System.out.println("\nFIFO scheduling results\n");
        printTraceTimes();

        while (!taskQueue.isEmpty()) {
            Task currentTask = taskQueue.peek();
            endTask = false;
            if (time >= currentTask.getArrivalTime()) {
                currentTask = taskQueue.poll();
                taskRunning = true;
                taskStart = time;
                tid = currentTask.getTaskID();
                at = currentTask.getArrivalTime();
                st = currentTask.getServiceTime();

                while (st != 0) {
                    for (Task t : taskQueue) {
                        if (!taskQueue.isEmpty()) {
                            if (time == t.getArrivalTime()) {
                                readyQueue.add(t);
                            }
                        }
                    }
                    if (!readyQueue.isEmpty()) {
                        System.out.printf("%4d%5c%1d%3c", time, tid, st, ' ');
                        printReadyQ(readyQueue);
                        System.out.println();
                    } else {
                        System.out.printf("%4d%5c%1d%5s\n", time, tid, st, emptyQ);
                    }
                    st--;
                    time++;
                }
                currentTask.setCompletionTime(time);
                endTask = true;
                completedTasks.addElement(currentTask);
                if (!readyQueue.isEmpty()) {
                    readyQueue.remove();
                }
            }
            if (!endTask) {
                System.out.printf("%4d%11s\n", time, emptyQ);
                time++;
            }
        }
        System.out.println();
    }

    private static void printReadyQ(Queue<Task> q){
        for(Task t: q){
            t.printTaskID(t);
        }
    }

    private static void buildTaskList() {
        int at = 0;
        int st = 0;
        char tid = '#';

        Task task;

        for (int i = 0; i < taskID.size(); i++) {
            tid = taskID.get(i);
            at = arrivalTime.get(i);
            st = serviceTime.get(i);

            task = new Task(tid, at, st);
            taskQueue.add(task);

        }
    }

    private static void sortQueueAsc(Queue<Task> q){
        int a;
        for(Task t: q){

        }

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
                arrivalTime.addElement(Integer.parseInt(a));

            } else {
                serviceTime.addElement(Integer.parseInt(a));
            }
            counter++;
        }
        input.close(); // close scanner to prevent leak

        // Create list of taskIDs
        for (int i = 0; i < arrivalTime.size(); i++) {
            taskID.addElement((char) (i + 65));
        }

        // convert both vectors to strings and print for debugging.
        arrivalTime.toString();
        serviceTime.toString();

        /** ARRIVAL TIMES */
        // System.out.println("Arrival times are:");
        // for (Integer element : arrivalTime) {
        // System.out.print(element + " ");
        // }
        // System.out.print("\n\r");

        /** SERVICE TIMES */
        // System.out.println("Service times are:");
        // for (Integer element : serviceTime) {
        // System.out.print(element + " ");
        // }
        // System.out.print("\n\r");

        /** TaskID */
        // System.out.println("Task IDs are:");
        // for (Character element : taskID) {
        // System.out.print(element + " ");
        // }
        // System.out.print("\n\r");

    }

    private static void printTraceTimes() {
        System.out.print("time   cpu   ready queue (tid/rst)\n----   ---   ---------------------\n\r");
    }

    private static void printTaskSummary() {
        System.out.println("Task summary table\n");
    }

    private static void printTimeSummary() {
        System.out.println(" service  wait\n  time    time\n" + " -------  ----");

    }
}
