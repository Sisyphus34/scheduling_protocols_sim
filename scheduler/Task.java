/**
 * Name: Shawn Picardy
 * Course: cpsc 3220
 * Smotherman
 * March 23th. 
 */

package scheduler;

import java.util.Comparator;

class Task implements Comparable<Task> {
    private char taskID;

    private int arrivalTime, serviceTime, remainingTime, completionTime, responseTime, waitTime;

    Task() {
    };

    Task(char tid, int at, int st) {
        taskID = tid;
        arrivalTime = at;
        serviceTime = st;
        remainingTime = st;
        completionTime = 0;
        responseTime = 0;
        waitTime = 0;
    }

    /**
     * @return the tid
     */
    public char getTaskID() {
        return taskID;
    }

    /**
     * @return the arrivalTime
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return the serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }

    /**
     * @return the remainingTime
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * @return the completionTime
     */
    public int getCompletionTime() {
        return completionTime;
    }

    /**
     * @return the responseTime
     */
    public int getResponseTime() {
        return responseTime;
    }

    /**
     * @return the waitTime
     */
    public int getWaitTime() {
        return waitTime;
    }

    /**
     * @param remainingTime the remainingTime to set
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**
     * @param completionTime the completionTime to set
     */
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * @param responseTime the responseTime to set
     */
    public void setResponseTime() {
        this.responseTime = this.completionTime - this.arrivalTime;
    }

    /**
     * @param waitTime the waitTime to set
     */
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void decTimeRemaining() {
        this.remainingTime--;
    }

    public void increaseWait() {
        this.waitTime++;
    }

    public void printTaskID() {
        char tid = this.getTaskID();
        int st = this.getServiceTime();
        System.out.print(tid + (Integer.toString(st)) + ", ");
    }

    public int compareTo(Task rhs) {
        if (this.getServiceTime() == rhs.getServiceTime()) {
            return Integer.compare(this.getWaitTime(), rhs.getWaitTime());
        }
        return Integer.compare(this.getServiceTime(), rhs.getServiceTime());
    }
}

// Helper class used to define sorting algorithm for use in
// Scheduling.sortReadyQueue()
class SortByShortest implements Comparator<Task> {
    public int compare(Task a, Task b) {
        return Integer.compare(a.getRemainingTime(), b.getRemainingTime());
    }
}
