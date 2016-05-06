package myapp.fibo.testutil;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    Instant startTime = Instant.now();

    /**
     * Restart the timer.
     */
    public void start() {
        this.startTime = Instant.now();
    }

    /**
     * Return the number of seconds until this method is called.
     * @return
     */
    public long end() {
        return Duration.between(startTime, Instant.now()).getSeconds();
    }
}
