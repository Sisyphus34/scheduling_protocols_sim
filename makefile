default:
	javac scheduler/Scheduler.java scheduler/Task.java
run:
	java scheduler/Scheduler < in1.txt
clean:
	rm  -f ./scheduler/*.class