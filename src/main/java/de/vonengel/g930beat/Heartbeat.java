/*
 * Copyright (c) 2015 Axel von Engel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.vonengel.g930beat;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.google.common.io.Resources;

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

    private URL getFileUrl() throws MalformedURLException {
        Path path = Paths.get(preferences.getFile());
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            return path.toUri().toURL();
        }
        return Resources.getResource(preferences.getFile());
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