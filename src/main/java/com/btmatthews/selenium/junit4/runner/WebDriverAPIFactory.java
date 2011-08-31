package com.btmatthews.selenium.junit4.runner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;

import com.thoughtworks.selenium.Selenium;

public class WebDriverAPIFactory implements SeleniumAPIFactory {

	private WebDriverConfiguration configuration;

	private Class<? extends WebDriver> webDriverClass;

	public WebDriverAPIFactory(final WebDriverConfiguration config,
			final Class<? extends WebDriver> driverClass) {
		configuration = config;
		webDriverClass = driverClass;
	}

	public Selenium create() throws Exception {
		final WebDriver webDriver = webDriverClass.newInstance();
		final Selenium api = new WebDriverBackedSelenium(webDriver,
				configuration.browserURL());
		return api;
	}

	public void start(final Selenium api)  {
	}

	public void stop(final Selenium api)  {
	}

}
