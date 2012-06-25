/*
 * Copyright 2011-2012 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.selenium.junit4.runner;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Factory that is responsible for creating the {@link Selenium} instance and
 * acting as a an wrapper for the start and stop methods.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public final class ServerFactory implements SeleniumFactory<Selenium> {

	/**
	 * The annotation that provides configuration for the test runner.
	 */
	private final ServerConfiguration configuration;

	/**
	 * The browser start command (e.g. {@literal "*firefox"}.
	 */
	private final String browserStartCommand;

	/**
	 * Construct the factory for creating {@link Selenium} instances.
	 * 
	 * @param config
	 *            The {@link ServerConfiguration} annotation that provides
	 *            configuration for the test runner.
	 * @param startCommand
	 *            The browser start command (e.g. {@literal "*firefox"}).
	 */
	public ServerFactory(final ServerConfiguration config,
			final String startCommand) {
		configuration = config;
		browserStartCommand = startCommand;
	}

	/**
	 * Return a string to identify the browser by trimming the leading * from
	 * the {@code browserStartCommand}.
	 * 
	 * @return The derived browser identification string.
	 */
	public String getBrowser() {
		String browser;
		if (StringUtils.startsWith(browserStartCommand, "*")) {
			browser = browserStartCommand.substring(1);
		} else {
			browser = StringUtils.EMPTY;
		}
		return browser;
	}

	/**
	 * Create a connection to the the Selenium Server at the host and port
	 * address specified by configuration annotation.
	 * 
	 * @return A {@link DefaultSelenium} object.
	 */
	public Selenium create() {
		return new DefaultSelenium(configuration.serverHost(),
				configuration.serverPort(), browserStartCommand,
				configuration.browserURL());
	}

	/**
	 * Connect to the Selenium Server.
	 * 
	 * @param server
	 *            The Selenium Server.
	 * @see SeleniumFactory#start(Object)
	 */
	public void start(final Selenium server) {
		server.start();
	}

	/**
	 * Disconnect from the Selenium Server.
	 * 
	 * @param server
	 *            The Selenium Server.
	 * @see SeleniumFactory#stop(Object)
	 */
	public void stop(final Selenium server) {
		server.stop();
	}
}
