
package weshampson.timekeeper.clock;

import java.util.Calendar;
import java.util.Date;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 4, 2014)
 * @since   0.3.0 (Nov 4, 2014)
 */
public class Clock {
    private final Calendar calendar = Calendar.getInstance();
    private volatile boolean isRunning = false;
    private Runnable clockRunnable;
    private Runnable clockTask;
    private Runnable midnightTask;
    public Clock(final Runnable clockTask, final int frequency) {
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        calendar.setTime(new Date(System.currentTimeMillis()));
                        execClockTask();
                        if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
                            execAtMidnight();
                        }
                        Thread.sleep(frequency);
                    } catch (InterruptedException ex) {
                        Logger.log(Level.ERROR, ex, null);
                    }
                }
            }
        };
        this.clockTask = clockTask;
    }
    private void execClockTask() {
        clockTask.run();
    }
    private void execAtMidnight() {
        if (midnightTask != null) {
            midnightTask.run();
        }
    }
    public synchronized void setMidnightTask(Runnable midnightTask) {
        this.midnightTask = midnightTask;
    }
    public synchronized void startClock() {
        isRunning = true;
        Thread clockThread = new Thread(clockRunnable);
        clockThread.setName("Clock");
        clockThread.start();
    }
    public synchronized void stopClock() {
        isRunning = false;
    }
}
