package com.jtestrunner.core;


import com.jtestrunner.Driver;

/**
 * Created by yusufaltun on 20/08/2017.
 */
public class Component
{
    private Driver driver;

    public Component(Driver driver)
    {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}
