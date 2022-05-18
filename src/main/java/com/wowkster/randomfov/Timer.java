package com.wowkster.randomfov;

public class Timer {
    long startTime;
    boolean started;
    long currentPeriod;
    long elapsedTime;

    public Timer() {

    }

    public void reset() {
        started = false;
        currentPeriod = 0L;
        elapsedTime = 0L;
        startTime = System.currentTimeMillis();
    }

    public void start() {
        if (started) return;
        started = true;
        startTime = System.currentTimeMillis();
        currentPeriod = 0;
    }

    public void stop() {
        if (!started) return;
        started = false;
        currentPeriod = System.currentTimeMillis() - startTime;
        elapsedTime += currentPeriod;
    }

    public long get() {
        if (!started) return elapsedTime;

        currentPeriod = System.currentTimeMillis() - startTime;
        return  elapsedTime + currentPeriod;
    }

    public double getSeconds() {
        if (!started) return elapsedTime / 1000.0D;

        currentPeriod = System.currentTimeMillis() - startTime;
        return  (elapsedTime + currentPeriod) / 1000.0D;
    }

    public boolean hasStarted() {
        return started;
    }

    public long getLap() {
        get();
        return currentPeriod;
    }
}

