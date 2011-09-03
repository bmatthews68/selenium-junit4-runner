/*
 * Copyright 2011 Brian Matthews
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

import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Factory that is responsible for creating the {@link WebDriver} instance and
 * acting as a an wrapper for the start and stop methods.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class ServerFactory implements SeleniumFactory<Selenium> {

	/**
	 * 
	 */
	private ServerConfiguration configuration;

	private String browserStartCommand;

	public ServerFactory(final ServerConfiguration config,
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
