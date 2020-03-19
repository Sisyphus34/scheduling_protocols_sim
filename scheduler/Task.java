
package scheduler;

class Task {
    private char taskID;

    private int arrivalTime, serviceTime, completionTime, responseTime, waitTime;

    Task() {
    };

    Task(char tid, int at, int st) {
        taskID = tid;
        arrivalTime = at;
        serviceTime = st;
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
     * @param completionTime the completionTime to set
     */
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * @param responseTime the responseTime to set
     */
    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * @param waitTime the waitTime to set
     */
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void printTask(Task t) {
        System.out.println("Task ID = " + t.getTaskID());
        System.out.println("Task arrival time = " + t.getArrivalTime());
        System.out.println("Task service time = " + t.getServiceTime());
        System.out.println("Completion time = " + t.getCompletionTime());
        System.out.println("Respoonse time = " + t.getResponseTime());
        System.out.println("Wait time = " + t.getWaitTime());

        System.out.println();
    }

    public void printTaskID(Task t) {
        char tid = t.getTaskID();
        int st = t.getServiceTime();
        System.out.print(tid + (Integer.toString(st)) + ", ");
    }
}