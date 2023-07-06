package com.jtestrunner;

import com.jtestrunner.core.ClassLoader;
import com.jtestrunner.core.Page;
import com.jtestrunner.core.api.client.ApiClient;
import com.jtestrunner.model.Step;
import com.jtestrunner.model.StepType;
import com.jtestrunner.util.StringUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//import org.openqa.selenium.security.UserAndPassword;
//import org.openqa.selenium.support.ui.ExpectedConditions;

public class Driver
{
	private WebDriver webDriver;
	private Page currentPage;
	private final Page defaultPage;
	private static ChromeDriverService service;

	public Driver(Page defaultPage)
	{
		this.defaultPage = defaultPage;
	}

	public Page getPage()
	{
		if(currentPage == null)
			currentPage = defaultPage;
		return currentPage;
	}

	public Driver goTo(Page page)
	{
		this.currentPage = page;
		webDriver.get(page.getUrl());
		currentPage.setDriver(this);
		page.waitForPage();
		PageFactory.initElements(webDriver, currentPage);

		return this;
	}

	public Driver fluentWait(By element)
	{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver())
				.withTimeout(Duration.ofSeconds(20))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class);

		WebElement aboutMe= wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(element);
			}
		});
		return this;
	}
	public Driver goTo(String url)
	{
		webDriver.get(url);
		detectAndGet();
		currentPage.setDriver(this);
		PageFactory.initElements(webDriver, currentPage);

		Step step = new Step();
		step.setStepType(StepType.OPEN);
		step.setDate(new Date());
		step.setDescription("GO TO "+ webDriver.getCurrentUrl());
		ApiClient.getInstance().getTestInfo().getSteps().add(step);

		return this;
	}

	public Driver mouseAction(String name)
	{
		WebElement element = get(name);
		new Actions(getWebDriver())
				.contextClick(element)
				.perform();

		return this;
	}
	public Driver click(String name)
	{
		try
		{
			WebElement element = get(name);
			WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		}catch (ElementClickInterceptedException e )
		{
			WebElement myelement = get(name);
			JavascriptExecutor jse2 = (JavascriptExecutor)getWebDriver();
			jse2.executeScript("arguments[0].scrollIntoView()", myelement);
		}
		waitForAjax();
		detectAndGet();

		Step step = new Step();
		step.setStepType(StepType.CLICK);
		step.setDate(new Date());
		step.setDescription("Clicked to  "+ name);
		ApiClient.getInstance().getTestInfo().getSteps().add(step);

		return this;
	}

	public Driver scrollToElement(String element)
    {
        WebElement myelement = get(element);
        JavascriptExecutor jse2 = (JavascriptExecutor)getWebDriver();
        jse2.executeScript("arguments[0].scrollIntoView()", myelement);

        return this;
    }

	public void detectAndGet()
	{
		String url = webDriver.getCurrentUrl().split("\\?")[0];
		if(getPage().getUrl() != null)
		{
			if(!getPage().getUrl().equals(url) || !StringUtil.regexMatch(getPage().getUrl(), url))
			{
				Page page = null;
				try
				{
					page = ClassLoader.get(url);
					if(null != page)
					{
						currentPage = page;
						currentPage.setDriver(this);
						PageFactory.initElements(webDriver, page);
						System.out.println("Opened this page "+ webDriver.getCurrentUrl());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public Driver setText(String element, String text)
	{
		waitForAjax();
		WebElement webElement = get(element);
		try{
			webElement.clear();
		}catch (Exception e)
		{

		}
		webElement.sendKeys(text);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Step step = new Step();
		step.setStepType(StepType.SET_TEXT);
		step.setDate(new Date());
		step.setDescription("SET TEXT "+ element +" as "+ text);
		ApiClient.getInstance().getTestInfo().getSteps().add(step);

		return this;
	}

	public Driver setTextWithJs(String input, String text)
	{
		JavascriptExecutor js = (JavascriptExecutor)getWebDriver();
		js.executeScript(
				input +".value = '"+ text +"';");

		return this;
	}

	public Driver clickWithJs(String element)
	{
		JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
		js.executeScript(element +".click()");

		return this;
	}

	protected WebElement get(String fieldName)
	{
		WebElement element = null;
		Class<?> pageClass = this.currentPage.getClass();
		Field field = null;

		try {
			field = pageClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			element = (WebElement) field.get(currentPage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Exception on accessing field {"+fieldName+"} of page");
		}

		return element;
	}

	public void close()
	{
		try {
			webDriver.close();
			webDriver.quit();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Driver switchPage(Page page)
	{
		this.currentPage = page;
		PageFactory.initElements(webDriver, currentPage);
		return this;
	}

	public boolean elementIsExist(By by)
	{
		return  !webDriver.findElements(by).isEmpty();
	}

    public Driver waitForAjax() {
        while(true) {
            Boolean ajaxIsComplete = waitStatus();
            if (ajaxIsComplete) {
                return this;
            }

            try {
                Thread.sleep(150L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }
    }

	public boolean waitStatus() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMinutes(1));

        // wait for jQuery to load
        Function<WebDriver, Object> jQueryLoad = new Function<WebDriver, Object>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)((JavascriptExecutor)webDriver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        Function<WebDriver, Object> jsLoad = new Function<WebDriver, Object>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)webDriver).executeScript("return document.readyState")
                    .toString().equals("complete");
            }
        };

        return (Boolean) wait.until(jQueryLoad) &&  (Boolean)wait.until(jsLoad);
	}

	public Boolean elementIsVisible(String element)
	{
		WebElement webElement = get(element);
		return webElement.isDisplayed();
	}
	public Driver select(String element, String value)
	{
		WebElement webElement = get(element);
		Select select = new Select(webElement);
		select.selectByVisibleText(value);
		return this;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public Driver removeAttrOfReadOnlyFields()
	{
		List<WebElement> inputs = webDriver.findElements(By.tagName("input"));

		for (WebElement input : inputs) {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].removeAttribute('readonly','readonly')",input);
		}

		return this;
	}

	public String getSmsCode()
	{
		LogEntries logs = webDriver.manage().logs().get("browser");
		String smsCode = null;
		for (LogEntry logEntry : logs)
		{
			if(logEntry.getMessage().contains("Sms Code"))
			{
				String[] log = logEntry.getMessage().split("\"");
				smsCode = log[1].split(":")[1];
			}

		}
		return smsCode;
	}

	public boolean elementTextCompare(String element, String s) {
		WebElement webElement = get(element);
		return webElement.getText().trim().equals(s);
	}

	public Driver pressEnter(String element)
	{
		pressKey(element, Keys.ENTER);
		waitForAjax();

		return this;
	}

	public Driver pressKey(String element, Keys key)
	{
		waitForAjax();
		WebElement webElement = get(element);
		webElement.sendKeys(key);
		return this;
	}

	public Driver hover(String element)
	{
		Actions actions = new Actions(webDriver);
		WebElement webElement = get(element);
		actions.moveToElement(webElement).build().perform();
		return this;
	}

	public Driver screenshot() throws IOException {

		File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);


		//The below method will save the screen shot in d drive with name "screenshot.png"
		//FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
		FileUtils.copyFile(scrFile, new File("opt\\media\\screenshot.png"));
		return this;
	}

	public WebDriver getWebDriver()
	{
		return webDriver;
	}

	public void setBasicAuthentication(String userName, String password)
	{
//		WebDriverWait wait = new WebDriverWait(getWebDriver(), 10);
//		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
//		alert.authenticateUsing(new UserAndPassword(userName, password));
	}

	public void waitForPage(Class clz)
	{
		int tryCount = 0;
		while (tryCount < 5)
		{
			if(!clz.isInstance(currentPage))
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				detectAndGet();
				tryCount ++;
			}
			else {
				return;
			}
		}
	}

	public void waitForElement(By by)
	{
		int tryCount = 0;
		while (tryCount < 5)
		{
			List<WebElement> elements = getWebDriver().findElements(by);

			if(elements.size() > 0)
			{
				return;
			}
			else
				{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tryCount ++;
			}
		}
	}

//	public Driver setValue(String element, String value)
//	{
//		WebElement webElement = get(element);
//		//
//	}
}
