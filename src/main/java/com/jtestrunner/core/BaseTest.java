package com.jtestrunner.core;

import com.jtestrunner.Config;
import com.jtestrunner.Driver;
import com.jtestrunner.UrlConfig;
import com.jtestrunner.core.resolver.PackagePathResolver;
import com.jtestrunner.core.resolver.UrlResolver;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;

/**
 * Created by yusufaltun on 04/04/2017.
 */
public class BaseTest
{
    public Driver driver;

    public UrlResolver urlResolver;
    public PackagePathResolver packagePathResolver;
    public IDriver iDriver;

    public BaseTest(UrlResolver urlResolver, PackagePathResolver packagePathResolver, IDriver iDriver)
    {
        this.urlResolver = urlResolver;
        this.packagePathResolver = packagePathResolver;
        this.iDriver = iDriver;
    }

    public IntegrationTestWatcher watchman = new IntegrationTestWatcher();
    @Rule
    public Retry retry = new Retry(1);
    @Rule
    public RuleChain chain= RuleChain.emptyRuleChain().around(watchman)
                    .around(new ExternalResource() {
                        @Override
                        protected void before() throws Throwable
                        {
                            UrlConfig.BASE_URL = urlResolver.getBasicUrl(Config.SECURE, Config.URL);
                            UrlConfig.FULL_URL = urlResolver.getFullUrl(Config.SECURE, Config.URL);

                            iDriver.setUrlResolver(urlResolver);

                            ClassLoader.setPagePackagePath(packagePathResolver);
                            ClassLoader.init();
                            driver = DriverBuilder.build(iDriver);
                        };
                        @Override
                        protected void after() {
                            driver.close();
                        };
                    });



}
