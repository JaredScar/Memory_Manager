package com.jaredscarito.memory_manager.api;

public class Process {
    private int pid = 0; // Process ID
    private int arrivalTime = 0; // Time the process arrives
    private int burstsDone = 0; // How many bursts are done
    private int burstsRemain = 0; // How many bursts remain
    private int burstsCount = 0; // Total bursts
    private boolean completed = false; // Is the Job completed?
    private boolean active = false; // Is the Job active in a CPU?

    private int startTime = 0; // The time the Job gets started by a CPU
    private int turnAroundTime = 0; // The time counted while in the ready queue and while being done (starting from arrivalTime)
    private int waitTime = 0; // The time spent waiting in the ready queue while not being a completed job
    private double percentDone = 0.0; // Percent of the program being completed ((burstCount / burstsRemaining) * 100)

    /* Priority */
    private int priority;

    public Process() {}

    public Process(int pid, int arrivalTime, int burstsCount) {}
}
