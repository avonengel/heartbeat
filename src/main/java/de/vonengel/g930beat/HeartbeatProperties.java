package de.vonengel.g930beat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HeartbeatProperties {
    public static final String FILE = "file";
    public static final String MIXER = "mixer";
    public static final String PERIOD = "period";
    private static final HeartbeatProperties INSTANCE = new HeartbeatProperties();
    private final Properties properties;

    static long getPeriod() {
        return Long.parseLong(INSTANCE.properties.getProperty("period"));
    }

    private HeartbeatProperties() {
        this.properties = new Properties();
        try {
            InputStream stream = getClass().getClassLoader()
                    .getResourceAsStream("heartbeat930.properties");
            this.properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFile() {
        return INSTANCE.properties.getProperty("file");
    }

    public static String getMixerName() {
        return INSTANCE.properties.getProperty("mixer");
    }
}