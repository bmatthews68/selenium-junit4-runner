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

/**
 * Defines constants used to configure defaults.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
final class Constants {

	/**
	 * The default host name of the Selenium Server.
	 */
	public static final String SELENIUM_SERVER_HOST = "localhost";

	/**
	 * The default port number of the Selenium Server.
	 */
	public static final int SELENIUM_SERVER_PORT = 4444;

	/**
	 * The default browser start command.
	 */
	public static final String DEFAULT_START_COMMAND = "*firefox";

	/**
	 * Default constructor is private.
	 */
	private Constants() {
	}
}