package com.smpete.heartrate.timer;

public interface TimerListener {
    public void timeUpdated(long millis);
    public void stateUpdate(int state);
    public void repUpdate(int rep);
}
