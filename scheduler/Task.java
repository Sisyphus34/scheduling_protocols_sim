
package scheduler;

class Task implements Comparable<Task> {
    private char taskID;

    private int arrivalTime, serviceTime, startTime, remainingTime, completionTime, responseTime, waitTime;

    Task() {
    };

    Task(char tid, int at, int st) {
        taskID = tid;
        arrivalTime = at;
        serviceTime = st;
        startTime = -1;
        remainingTime = st;
        completionTime = -1;
        responseTime = -1;
        waitTime = -1;
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
     * @return the startTime
     */
    public int getStartTime() {
        return startTime;
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
     * @param startTime the startTime to set
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
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
    public void setWaitTime() {
        this.waitTime = this.startTime - this.arrivalTime;
    }

    public void decTimeRemaining() {
        this.remainingTime--;
    }

    public void printTask(Task t) {
        System.out.println("Task ID = " + t.getTaskID());
        System.out.println("Task arrival time = " + t.getArrivalTime());
        System.out.println("Task service time = " + t.getServiceTime());
        System.out.println("Start time = " + t.getStartTime());
        System.out.println("Remaining time = " + t.getRemainingTime());
        System.out.println("Completion time = " + t.getCompletionTime());
        System.out.println("Respoonse time = " + t.getResponseTime());
        System.out.println("Wait time = " + t.getWaitTime());

        System.out.println();
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