
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
    private static boolean taskComplete = false;
    private static String emptyQ = "--";


    public static void main(String[] args) {
        acquireTimes();
        buildTaskQueue();
        callFifo();
        printTaskSummary();
        printTimeSummary();
    }

    private static void callFifo() {

        System.out.println("\nFIFO scheduling results\n");
        printTraceTimes();

        while (!taskQueue.isEmpty()) {

            // Set current task = head of task queue WITHOUT removing from task queue
            Task currentTask = taskQueue.peek();

            // Ctrl to prevent redundant print lines while no task is "running"
            taskComplete = false;

            // Start task if task arrival time = time OR if task is in the ready queue
            if (time >= currentTask.getArrivalTime()) {
                // Set the current task = head of task queue WITH removal from task queue since
                // it is now "running"
                currentTask = taskQueue.poll();

                // Task summary variables
                taskStart = time; // used to compute task wait time
                tid = currentTask.getTaskID(); // current task id
                at = currentTask.getArrivalTime(); // current task arrival time
                st = currentTask.getServiceTime(); // current task service time

                // While the task still has service time, run task
                while (st != 0) {

                    // Dynamically track if incoming tasks should be added to ready queue
                    for (Task t : taskQueue) {
                        if (!taskQueue.isEmpty()) {
                            if (time == t.getArrivalTime()) {
                                readyQueue.add(t); // add incoming task to ready queue
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
                taskComplete = true;
                completedTasks.addElement(currentTask);
                if (!readyQueue.isEmpty()) {
                    readyQueue.remove();
                }
            }
            if (!taskComplete) {
                System.out.printf("%4d%11s\n", time, emptyQ);
                time++;
            }
        }
        System.out.println();
    }

    private static void printReadyQ(Queue<Task> q) {
        for (Task t : q) {
            t.printTaskID(t);
        }
    }

    private static void buildTaskQueue() {

        // generic task variables to be replaced
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

    // private static void sortQueueAsc(Queue<Task> q) {
    //     int a;
    //     for (Task t : q) {

    //     }

    // }

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
