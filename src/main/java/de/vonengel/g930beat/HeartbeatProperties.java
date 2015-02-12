package de.vonengel.g930beat;


public class HeartbeatProperties {
    public static final String FILE = "file";
    public static final String MIXER = "mixer";
    public static final String PERIOD = "period";
    private static final HeartbeatProperties INSTANCE = new HeartbeatProperties();
//    private final Properties properties;

    static long getPeriod() {
        return 10;
    }

    private HeartbeatProperties() {
//        this.properties = new Properties();
//        try {
//            InputStream stream = getClass().getClassLoader()
//                    .getResourceAsStream("heartbeat930.properties");
//            this.properties.load(stream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static String getFile() {
        return "timer_bell_or_desk_bell_ringing.wav";
    }

//    public static String getMixerName() {
//        return INSTANCE.properties.getProperty("mixer");
//    }
}