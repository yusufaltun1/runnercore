package com.jtestrunner.core;

import com.jtestrunner.Driver;
import com.jtestrunner.UrlConfig;
import com.jtestrunner.model.ModuleType;

public abstract class Page
{
	private String pageUrl;
	private Driver driver;

	public Page(String pageUrl)
	{
		this.pageUrl = pageUrl;
	}
	public Page(){}
	public String getUrl()
	{
		if(pageUrl == null)
		{
			PageType pageType = getClass().getAnnotation(PageType.class);

			if(ModuleType.STOREFRONT.equals(pageType.module()))
			{
				pageUrl = UrlConfig.FULL_URL + pageType.url();
			}
			else if(ModuleType.HAC.equals(pageType.module()))
			{
				pageUrl = UrlConfig.BASE_URL +"/hac"+ pageType.url();
			}
			else {
				pageUrl = pageType.url();
			}
		}
		return pageUrl;
	}

	public void waitForPage(){}

	protected Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.pageUrl = url;
	}
}
