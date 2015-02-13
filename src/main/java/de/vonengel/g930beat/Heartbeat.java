package de.vonengel.g930beat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Heartbeat {
    private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);
    private Map<String, Timer> runningBeats = new HashMap<>();
    private HeartbeatPreferences preferences = new HeartbeatPreferences();

    public void start(String mixerName) throws Exception {
        AudioInputStream ais = loadAudioInput();
        Mixer.Info mixerInfo = pickMixer(mixerName);
        final Clip clip = AudioSystem.getClip(mixerInfo);
        clip.open(ais);
        logParameters(mixerInfo);
        TimerTask task = new TimerTask() {
            public void run() {
                Heartbeat.LOG.info("Sending heartbeat");
                try {
                    clip.setFramePosition(0);
                    clip.start();
                } catch (Exception e) {
                    LOG.error("Error playing clip", e);
                    clip.close();
                }
            }
        };
        Timer timer = new Timer("Heartbeat930-" + mixerName, true);
        runningBeats.put(mixerName, timer);
        timer.scheduleAtFixedRate(task, 0L, computePeriod());
    }

    public Mixer.Info pickMixer(String desiredName) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals(desiredName)) {
                return info;
            }
        }
        throw new NoSuchElementException(desiredName);
    }

    private void logParameters(Mixer.Info mixerInfo) {
        LOG.info("Starting heartbeat:");
        LOG.info("Mixer: {}", mixerInfo.getName());
        LOG.info("File: {}", preferences.getFile());
        LOG.info("Period: {}", Long.valueOf(preferences.getPeriod()));
    }

    public void stop() {
        runningBeats.forEach((name, timer) -> timer.cancel());
    }

    private AudioInputStream loadAudioInput() throws IOException, UnsupportedAudioFileException {
        URL url = super.getClass().getClassLoader().getResource(preferences.getFile());
        assert url != null;
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        return ais;
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
        Timer timer = runningBeats.get(mixerName);
        if (timer != null) {
            timer.cancel();
        }
    }
}