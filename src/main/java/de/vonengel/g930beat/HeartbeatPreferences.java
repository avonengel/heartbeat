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

import java.util.prefs.Preferences;

public class HeartbeatPreferences {
    public static final String FILE = "file";
    public static final String PERIOD = "period";

    public static final String DEFAULT_SOUND = "timer_bell_or_desk_bell_ringing.wav";
    /**
     * 20 minutes (in seconds): {@value #DEFAULT_PERIOD}
     */
    public static final int DEFAULT_PERIOD = 20 * 60;

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