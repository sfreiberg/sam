package com.allthingscompute.sam.monitor;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * @version 1.0
 * @author
 */
public class NoAlert implements AlertInterface
{
    private String alertType = "No alert";

    private String waveFile = "file:wav/danger.wav";

    private boolean playSound = true;

    AudioClip clip;

    public NoAlert()
    {
        try
        {
            clip = Applet.newAudioClip(new URL(waveFile));
        } catch (Exception e)
        {
            playSound = false;
        }
    }

    /*
     * @see AlertInterface#fireAlert()
     */
    public void fireAlert()
    {
        clip.play();

    }

    public String toString()
    {
        return alertType;
    }

}