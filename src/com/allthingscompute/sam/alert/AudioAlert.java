package com.allthingscompute.sam.alert;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

import com.allthingscompute.sam.log.Logger;

/**
 * This class plays an audio alert, although I found on my redhat system that
 * sound events are queued (due to the fact that I was listening to music while
 * testing this project), so the
 * {@link com.allthingscompute.sam.monitor.TimedMonitor#udpate update}method of
 * the {@link com.allthingscompute.sam.monitor.TimedMonitor TimedMonitor}object
 * was blocking due to the queueing of sounds on my system. So to fix this
 * issue, I have made it extend the thread object, so that it will be a single
 * thread that is blocking on the sound and not the
 * {@link com.allthingscompute.sam.monitor.TimedMonitor TimedMonitor}object.
 * 
 * @author Sam, Documented by E. Internicola
 * @see com.allthingscompute.sam.alert.AlertLevelChangeListener
 * @see com.allthingscompute.sam.monitor.TimedMonitor
 */
public class AudioAlert implements AlertLevelChangeListener
{
    /** */
    private File lowAlertAudioFile, medAlertAudioFile, highAlertAudioFile;

    /**
     * This constructor takes 3 string objects, that pertain to the filenames of
     * 3 audio files.
     * 
     * @see #AudioAlert(File,File,File)
     */
    public AudioAlert(String lowAlert, String medAlert, String highAlert)
    {
        this(new File(lowAlert), new File(medAlert), new File(highAlert));
    }

    /**
     * Constructor - takes 3 file objects, one for a "low alert" sound, one for
     * a "medium alert" sound, and the last for a "high alert" sound.
     */
    public AudioAlert(File lowAlert, File medAlert, File highAlert)
    {
        lowAlertAudioFile = lowAlert;
        medAlertAudioFile = medAlert;
        highAlertAudioFile = highAlert;
    }

    /**
     * When the alert level changes, this method is fired to queue the audio
     * alert message.
     */
    public void alertLevelChanged(int alertLevel)
    {
        SoundThread st = null;

        st = new SoundThread(lowAlertAudioFile, medAlertAudioFile,
                highAlertAudioFile, alertLevel);
        st.start();
    }

    /**
     * This returns the string "Audible Alert", probably for some check
     * elsewhere as to what type of alert this is (as this class is a subclass
     * of
     * {@link com.allthingscompute.sam.alert.AlertLevelChangeListener alertLevelChangeListener}).
     * I have found it good for debugging later on as it is a child class and
     * there are times that it's nice to see where the blocking call is coming
     * from ;-).
     */
    public String toString()
    {
        return "Audible Alert";
    }

    /**
     * Helper Thread - SoundThread - this plays the sound for us.
     */
    private class SoundThread extends Thread
    {
        private int alertLevel;

        private File lowAlertAudioFile, medAlertAudioFile, highAlertAudioFile;

        /**
         * Constructor.
         */
        SoundThread(File f1, File f2, File f3, int alertLevel)
        {
            this.alertLevel = alertLevel;
            lowAlertAudioFile = f1;
            medAlertAudioFile = f2;
            highAlertAudioFile = f3;
        }

        /**
         * This method just tries to play a sound, it is a thread in case the
         * sound is queued by the hardware.
         */
        public void run()
        {
            AudioClip audioClip = null;
            try
            {
                if (alertLevel == AlertLevel.LOW_ALERT_LEVEL)
                    audioClip = Applet.newAudioClip(lowAlertAudioFile.toURL());
                else if (alertLevel == AlertLevel.MEDIUM_ALERT_LEVEL)
                    audioClip = Applet.newAudioClip(medAlertAudioFile.toURL());
                else if (alertLevel == AlertLevel.HIGH_ALERT_LEVEL)
                    audioClip = Applet.newAudioClip(highAlertAudioFile.toURL());
            } catch (Exception e)
            {
                Logger.writeToLog(e.getMessage());
            }
            audioClip.play();
            audioClip.stop();
            audioClip = null;
        }
    }
}