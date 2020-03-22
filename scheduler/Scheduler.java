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
import java.util.Collections;
import java.util.stream.Collectors;

class Scheduler {

    private static Vector<Task> taskList = new Vector<Task>();
    private static Queue<Task> taskQueue = new LinkedList<Task>();
    private static Queue<Task> readyQueue = new LinkedList<Task>();
    private static Vector<Task> readyList = new Vector<Task>();

    private static int time = 0;
    private static String emptyQ = "--";

    public static void main(String[] args) {

        buildTaskList();
        buildTaskQueue();

        // if(fifo)
        // else if (sjn)
        // else if (rr)
        // else (c'mon prof...use correct input)
        // fifoScheduling();
        sjfScheduling();
        printTaskSummary(taskList);
        printTimeSummary();

        // for (Task t : taskList) {
        // t.printTask(t);
        // }
    }

    private static void fifoScheduling() {

        boolean taskComplete;

        System.out.println("\nFIFO scheduling results\n");
        printTraceHeader();

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

                // While the task still has remaining time, run task
                while (currentTask.getRemainingTime() != 0) {
                    // Dynamically track if incoming tasks should be added to ready queue
                    for (Task t : taskQueue) {
                        if (!taskQueue.isEmpty()) {
                            if (time == t.getArrivalTime()) {
                                readyQueue.add(t); // add incoming task to ready queue
                            }
                        }
                    }

                    // Print scheduling results:
                    // print format if any tasks are in ready queue
                    if (!readyQueue.isEmpty()) {
                        printTaskStats(currentTask);
                        printReadyQ(readyQueue);
                        System.out.println();
                    } else { // else print generic format
                        System.out.printf("%4d%5c%1d%5s\n", time, currentTask.getTaskID(),
                                currentTask.getRemainingTime(), emptyQ);
                    }
                    currentTask.decTimeRemaining(); // decrement remaining time for current task
                    increaseWaitTimes(readyQueue);
                    time++; // and time goes on...
                }
                currentTask.setCompletionTime(time);
                currentTask.setResponseTime();
                taskComplete = true;

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

    private static void sjfScheduling() {

        boolean taskComplete;

        System.out.println("\nSJF(preemptive) scheduling results\n");
        printTraceHeader();

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

                // While the task still has remaining time, run task
                while (currentTask.getRemainingTime() != 0) {
                    // Dynamically track if incoming tasks should be added to ready queue
                    for (Task t : taskQueue) {
                        if (!taskQueue.isEmpty()) {
                            // Add any additional arriving tasks to ready queue
                            // and remove those tasks from the task queue thus shifting ctrl
                            // of those tasks over to the ready queue
                            if (time == t.getArrivalTime()) {
                                readyQueue.add(t); // add additional task to ready queue
                            }
                        }
                    }
                    for (Task t : readyQueue) {
                        if (taskQueue.contains(t)) {
                            taskQueue.remove(t);
                        }
                    }

                    sortReadyQueue(); // sort the ready queue according to SJF(preemptive) protocol.

                    /**
                     * Find the task with lowest remaining time. The readyQueue's state is sorted by
                     * shortest remaining time at the head Compare the head of the ready queue to
                     * the current task to see which has a shorter amount of time then replace the
                     * current task if necessary.
                     */
                    if (!readyQueue.isEmpty()) {
                        if (readyQueue.peek().getRemainingTime() < currentTask.getRemainingTime()) {
                            readyQueue.add(currentTask);
                            currentTask = readyQueue.poll();
                        }
                    }

                    // Print scheduling results:
                    // print format if any tasks are in ready queue
                    if (!readyQueue.isEmpty()) {
                        printTaskStats(currentTask);
                        printReadyQ(readyQueue);
                        System.out.println();
                    } else { // else print generic format
                        System.out.printf("%4d%5c%1d%5s\n", time, currentTask.getTaskID(),
                                currentTask.getRemainingTime(), emptyQ);
                    }
                    currentTask.decTimeRemaining(); // decrement remaining time for current task
                    increaseWaitTimes(readyQueue);
                    time++; // and time goes on...
                }
                for (Task t : taskQueue) {
                    if (t.getRemainingTime() == 0) {
                        taskQueue.remove(t);
                    }
                }
                currentTask.setCompletionTime(time);
                currentTask.setResponseTime();
                taskComplete = true;

                if (taskQueue.isEmpty()) {
                    if (!readyQueue.isEmpty())
                        taskQueue.add(readyQueue.poll());
                } else {
                    if (!readyQueue.isEmpty())
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
            System.out.printf("%1c%1d%1s", t.getTaskID(), t.getRemainingTime(), ", ");
        }
    }

    private static void buildTaskQueue() {

        // Build task queue from tastList passing references for easy computations and
        // summaries

        for (Task t : taskList) {
            taskQueue.add(t);
        }
    }

    // Construct vector of all tasks
    private static void buildTaskList() {
        // Create scanner for I/O redirection
        Scanner input = new Scanner(System.in);

        // Variables to be passed to task constructor
        int counter = 0;
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
            counter++;
            tid++;
        }
        input.close(); // close scanner to prevent leak
    }

    private static void printTraceHeader() {
        System.out.print("time   cpu   ready queue (tid/rst)\n----   ---   ---------------------\n\r");
    }

    private static void printTaskSummary(Vector<Task> vec) {
        System.out.println("     arrival service completion response wait\n"
                + "tid   time    time      time      time   time\n" + "---  ------- ------- ---------- -------- ----");
        for (Task t : vec) {
            System.out.printf("%3c%6d%8d%10d%10d%7d\n", t.getTaskID(), t.getArrivalTime(), t.getServiceTime(),
                    t.getCompletionTime(), t.getResponseTime(), t.getWaitTime());
        }
        System.out.println();
    }

    private static void printTimeSummary() {
        System.out.println("service  wait\n  time   time\n" + "-------  ----");

        Collections.sort(taskList);
        for (Task t : taskList) {
            System.out.printf("%4d%7d\n", t.getServiceTime(), t.getWaitTime());
        }
        System.out.println();
    }

    private static void printTaskStats(Task t) {
        System.out.printf("%4d%5c%1d%3c", time, t.getTaskID(), t.getRemainingTime(), ' ');
    }

    private static void increaseWaitTimes(Queue<Task> q) {
        for (Task t : q) {
            t.increaseWait();
        }
    }

    private static void sortReadyQueue() {
        readyList.removeAllElements();

        while (!readyQueue.isEmpty()) {
            readyList.addElement(readyQueue.poll());
        }

        Collections.sort(readyList, new Sortbywait());
        readyQueue.addAll(readyList);

    }
}