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

import java.io.IOException;
import java.net.URL;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class BeatTask extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(BeatTask.class);
    private final Clip clip;
    private AudioInputStream ais;

    BeatTask(Info info, URL url) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        LOG.info("Starting heartbeat:");
        LOG.info("Mixer: {}", info.getName());
        this.clip = AudioSystem.getClip(info);
        ais = initAudioInput(url);
        clip.open(ais);
    }

    private AudioInputStream initAudioInput(URL url) throws IOException, UnsupportedAudioFileException {
        assert url != null;
        LOG.debug("loading audio from '{}'", url);
        return AudioSystem.getAudioInputStream(url);
    }

    public void run() {
        LOG.info("Sending heartbeat");
        try {
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            LOG.error("Error playing clip", e);
            closeClip();
        }
    }

    @Override
    public boolean cancel() {
        closeClip();
        return super.cancel();
    }

    private void closeClip() {
        if (clip.isOpen()) {
            clip.close();
            try {
                ais.close();
            } catch (IOException e) {
                LOG.error("Error closing AudioInputStream", e);
            }
        }
    }
}