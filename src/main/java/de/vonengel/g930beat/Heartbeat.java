package de.vonengel.g930beat;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Heartbeat {
    private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);
    private Map<String, TimerTask> activeBeats = new HashMap<>();
    private HeartbeatPreferences preferences;
    private Timer timer;

    public Heartbeat() {
        timer = new Timer("Heartbeat930", true);
    }

    public void start(String mixerName) throws Exception {
        Mixer.Info mixerInfo = pickMixer(mixerName);
        TimerTask task = new BeatTask(mixerInfo, getFileUrl());
        activeBeats.put(mixerName, task);
        timer.scheduleAtFixedRate(task, 0L, computePeriod());
    }

    private URL getFileUrl() {
        return getClass().getClassLoader().getResource(preferences.getFile());
    }

    public Mixer.Info pickMixer(String desiredName) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals(desiredName)) {
                return info;
            }
        }
        throw new NoSuchElementException(desiredName);
    }

    public void stop() {
        activeBeats.forEach((name, beat) -> beat.cancel());
    }

    private long computePeriod() {
        return (preferences.getPeriod() * 1000L);
    }

    public List<String> getAvailableMixers() {
        List<String> result = new ArrayList<>();
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, null);
        for (Info mixerInfo : AudioSystem.getMixerInfo()) {
            if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
                LOG.debug("Mixer '{}' supports the lineInfo", mixerInfo);
                result.add(mixerInfo.getName());
            }
        }
        return result;
    }

    public void cancelMixer(String mixerName) {
        TimerTask beatTask = activeBeats.get(mixerName);
        if (beatTask != null) {
            beatTask.cancel();
        }
    }
    
    public void setPreferences(HeartbeatPreferences preferences) {
        this.preferences = preferences;
    }
}