package com.allthingscompute.sam.scheduler;

/**
 * @author Sam
 */
public interface Job
{
    public void init();

    public void execute();
}