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