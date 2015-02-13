package de.vonengel.g930beat;

import java.util.prefs.Preferences;

public class HeartbeatPreferences {
    public static final String FILE = "file";
    public static final String PERIOD = "period";

    private static final String DEFAULT_SOUND = "timer_bell_or_desk_bell_ringing.wav";
    /**
     * 20 minutes (in seconds): {@value #DEFAULT_PERIOD}
     */
    private static final int DEFAULT_PERIOD = 20 * 60;

    private Preferences preferences = Preferences.userNodeForPackage(HeartbeatPreferences.class);

    protected HeartbeatPreferences() {
    }

    public long getPeriod() {
        return preferences.getLong(PERIOD, DEFAULT_PERIOD);
    }

    public String getFile() {
        return preferences.get(FILE, DEFAULT_SOUND);
    }

    public void setPeriod(long period) {
        preferences.putLong(PERIOD, period);
    }

    public void setFile(String file) {
        preferences.put(FILE, file);
    }
}