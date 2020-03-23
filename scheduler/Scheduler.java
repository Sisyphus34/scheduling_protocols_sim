/**
 * Name: Shawn Picardy
 * Course: cpsc 3220
 * Smotherman
 * March 23th. 
 */

package scheduler;

import java.util.Scanner;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;

class Scheduler {

    private static Vector<Task> taskList = new Vector<Task>();
    private static Queue<Task> taskQueue = new LinkedList<Task>();
    private static Vector<Task> readyList = new Vector<Task>();
    private static Queue<Task> readyQueue = new LinkedList<Task>();
    private static int time = 0;
    private static String emptyQ = "--";

    public static void main(String[] args) {

        buildTaskList();
        buildTaskQueue();

        if (args[0].equalsIgnoreCase("-fifo")) {
            System.out.println("\nFIFO scheduling results\n");
            printTaskHeader();
            runScheduler(args[0]);
        } else if (args[0].equalsIgnoreCase("-sjf")) {
            System.out.println("\nSJF(preemptive) scheduling results\n");
            printTaskHeader();
            runScheduler(args[0]);
        } else if (args[0].equalsIgnoreCase("-rr")) {
            System.out.println("\nRR scheduling results (time slice is 1)\n");
            printTaskHeader();
            runScheduler(args[0]);
        } else
            System.out.println("C'mon professor...input a valid argument");

        printTaskSummary(taskList);
        printTimeSummary();
    }

    private static void runScheduler(String type) {

        Task currentTask = null;
        boolean running = true;

        while (running) {
            // Load all tasks with current arrival time into ready queue
            for (Task t : taskQueue) {
                if (t.getArrivalTime() == time) {
                    readyQueue.add(t);
                }
            }
            // Remove tasks loaded into readyQueue from taskQueue
            for (Task t : readyQueue) {
                taskQueue.remove(t);
            }

            // ========================THIS IS THE IMPORTANT STUFF========================

            // If FIFO SCHEDULING: load head of ready queue into current task if no task is
            // running
            if (type.equalsIgnoreCase("-fifo")) {
                if (currentTask == null) {
                    currentTask = readyQueue.poll();
                }
            }
            // If SJF SCHEDULING: sort the ready queue by shortest remaining time, then load
            // the head of ready queue into current task
            if (type.equalsIgnoreCase("-sjf")) {
                sortReadyQueue();
                if (currentTask == null) {
                    currentTask = readyQueue.poll();
                }
            }
            // If RR SCHEDULING: add the current task to the back of readyQueue
            // and start task at head of readyQueue.
            if (type.equalsIgnoreCase("-rr")) {
                // If no task is runinng, then then start task at head of readyQueue.
                if (currentTask == null) {
                    currentTask = readyQueue.poll();
                } else {
                    readyQueue.add(currentTask);
                    currentTask = readyQueue.poll();
                }
            }
            // ========================THIS ENDS THE IMPORTANT STUFF========================

            // Print scheduling results for null tasks
            if (currentTask == null) {
                System.out.printf("%4d%11s\n", time, emptyQ);
                time++;
            } else { // Print scheduling results for current task and set time stamps
                if (readyQueue.isEmpty()) {
                    System.out.printf("%4d%5c%1d%5s\n", time, currentTask.getTaskID(), currentTask.getRemainingTime(),
                            emptyQ);
                } else {
                    printTaskStats(currentTask);
                    printReadyQ(readyQueue);
                    System.out.println();
                }
                currentTask.decTimeRemaining(); // decrement remaining time for current task
                increaseWaitTimes(readyQueue); // increment wait times of tasks in readyQueue
                time++; // and time goes on...
                // If the current task is finished, then set time stamps and remove task
                if (currentTask.getRemainingTime() == 0) {
                    currentTask.setCompletionTime(time);
                    currentTask.setResponseTime();
                    currentTask = null;
                }
            }
            // If the simulation is over, then stop simulation.
            if (taskQueue.isEmpty() && readyQueue.isEmpty() && currentTask == null) {
                running = false;
            }
        }
        System.out.println();
    }

    // Construct vector of all tasks
    // I chose to use a vector for easy sorting
    // The values in this list are pulled for final summaries
    private static void buildTaskList() {
        // Create scanner for I/O redirection
        Scanner input = new Scanner(System.in);
        // Variables to be passed to task constructor
        char tid = 'A'; // initial task is labeled 'A'
        int at;
        int st;
        String[] line;
        while (input.hasNextLine()) {
            // parse ints from each line then build task with tid, at, and st
            line = input.nextLine().split(" ");
            if (!line[0].isEmpty()) {
                at = Integer.parseInt(line[0]);
                st = Integer.parseInt(line[1]);
                Task task = new Task(tid, at, st);
                taskList.addElement(task);
            } else
                break;
            tid++;
        }
        input.close(); // close scanner to prevent leak
    }

    // Build taskQueue from tastList passing references so any updates to the queue
    // will also occur in the list. Useful for final computations and sorting
    private static void buildTaskQueue() {
        for (Task t : taskList) {
            taskQueue.add(t);
        }
    }

    // Print the readyQueue
    private static void printReadyQ(Queue<Task> q) {
        for (Task t : q) {
            System.out.printf("%1c%1d%1s", t.getTaskID(), t.getRemainingTime(), ", ");
        }
    }

    // Print the task header
    private static void printTaskHeader() {
        System.out.print("time   cpu   ready queue (tid/rst)\n----   ---   ---------------------\n\r");
    }

    // Print the task summary
    private static void printTaskSummary(Vector<Task> vec) {
        System.out.println("     arrival service completion response wait\n"
                + "tid   time    time      time      time   time\n" + "---  ------- ------- ---------- -------- ----");
        for (Task t : vec) {
            System.out.printf("%3c%6d%8d%10d%10d%7d\n", t.getTaskID(), t.getArrivalTime(), t.getServiceTime(),
                    t.getCompletionTime(), t.getResponseTime(), t.getWaitTime());
        }
        System.out.println();
    }

    // Print the time summary.
    // NOTE: custom sorting method called:
    // "Collection.sort()" where sorting algorithm is defined in Task.compareTo()
    private static void printTimeSummary() {
        System.out.println("service  wait\n  time   time\n" + "-------  ----");
        Collections.sort(taskList);
        for (Task t : taskList) {
            System.out.printf("%4d%7d\n", t.getServiceTime(), t.getWaitTime());
        }
        System.out.println();
    }

    // Print the task stats each iteration
    private static void printTaskStats(Task t) {
        System.out.printf("%4d%5c%1d%3c", time, t.getTaskID(), t.getRemainingTime(), ' ');
    }

    // Increase the wait times for any task in the queue parameter
    private static void increaseWaitTimes(Queue<Task> q) {
        for (Task t : q) {
            t.increaseWait();
        }
    }

    // Sort the ready queue by shortest remainging time.
    // NOTE: custom sorting method called:
    // "Collection.sort()" where sorting algorithm is defined in
    // SortByShortest.compare()
    private static void sortReadyQueue() {
        readyList.removeAllElements();
        while (!readyQueue.isEmpty()) {
            readyList.addElement(readyQueue.poll());
        }
        Collections.sort(readyList, new SortByShortest());
        readyQueue.addAll(readyList);
    }
}