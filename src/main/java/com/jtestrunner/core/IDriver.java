package com.jtestrunner.core;


import com.jtestrunner.Driver;
import com.jtestrunner.core.resolver.UrlResolver;

/**
 * Created by yusufaltun on 22.11.2017.
 */
public interface IDriver
{
    Driver getDriver() throws Exception;
    void setUrlResolver(UrlResolver urlResolver);
}
