package com.btmatthews.selenium.junit4.runner;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeleniumRCAPIFactory implements SeleniumAPIFactory {

	private SeleniumRCConfiguration configuration;

	private String browserStartCommand;

	public SeleniumRCAPIFactory(final SeleniumRCConfiguration config,
			final String startCommand) {
		configuration = config;
		browserStartCommand = startCommand;
	}

	public Selenium create() {
		final Selenium api = new DefaultSelenium(configuration.serverHost(),
				configuration.serverPort(), browserStartCommand,
				configuration.browserURL());
		return api;
	}

	public void start(final Selenium api) {
		api.start();
	}

	public void stop(final Selenium api) {
		api.stop();
	}

}
