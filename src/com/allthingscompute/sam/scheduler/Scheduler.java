package com.allthingscompute.sam.scheduler;

import java.util.ArrayList;

import com.allthingscompute.sam.log.Logger;

/**
 * This class seems to be aroudn to schedule threads, although it isn't doing
 * anything at the moment.
 * 
 * @author Sam, Documented by E. Internicola
 */
public class Scheduler extends Thread
{
    private ArrayList jobs = new ArrayList();

    private boolean runJobs = false;

    private boolean keepRunning = true;

    private long timeToSleep = 1000l;

    /**
     * Public method to retreive the ArrayList of jobs.
     */
    public ArrayList getJobs()
    {
        return jobs;
    }

    /**
     * Public method to add a job to the ArrayList of jobs.
     */
    public void addJob(Job j)
    {
        jobs.add(j);
    }

    /**
     * Public method to remove a job from the ArrayList of jobs.
     */
    public void removeJob(Job j)
    {
        jobs.remove(j);
    }

    /**
     * Main loop - you run this method by calling the "start" method.
     */
    public void run()
    {
        while (keepRunning)
        {
            if (runJobs)
            {
                for (int i = 0; i < jobs.size(); i++)
                {
                    Job j = (Job) jobs.get(i);
                    j.execute();
                }
            }
            try
            {
                sleep(timeToSleep);
            } catch (InterruptedException e)
            {
                Logger.writeToLog(e.getMessage());
            }
        }
    }
}