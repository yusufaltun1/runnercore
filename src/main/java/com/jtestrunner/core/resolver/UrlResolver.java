package com.jtestrunner.core.resolver;

/**
 * Created by yusufaltun on 11/10/2017.
 */
public interface UrlResolver
{
    String getBasicUrl(boolean secure, String url);
    String getFullUrl(boolean secure, String url);
    String getHomePageUrl();
}
