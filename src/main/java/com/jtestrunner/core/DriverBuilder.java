package com.jtestrunner.core;


import com.jtestrunner.Driver;

/**
 * Created by yusufaltun on 05/04/2017.
 */
public class DriverBuilder
{

    public static Driver build(IDriver driver) throws Exception
    {
        return driver.getDriver();
    }

}
