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

/**
 * Generic interface that must be implemented by the factories that create the
 * Selenium Server, Web Driver and Wrapped Web Driver objects.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 * 
 * @param <T>
 *            <ul>
 *            <li>{@link Selenium} for Selenium Server and Wrapped Web Driver
 *            factories</li>
 *            <li>{@link WebDriver} for Web Driver factories</li>
 *            </ul>
 */
public interface SeleniumFactory<T> {

	/**
	 * Return a string to identify the browser.
	 * 
	 * @return A browser identification string.
	 */
	String getBrowser();

	/**
	 * Create the Selenium Server, Web Driver or Wrapped Web Driver object.
	 * 
	 * @return A instance of {@code T}.
	 * @throws Exception
	 *             IF there was an problem creating the object.
	 */
	T create() throws Exception;

	/**
	 * Connect to the Selenium Server or start the Web Browser used to execute
	 * the tests.
	 * 
	 * @param object
	 *            The object used to communicate with the Selenium Server or Web
	 *            Browser.
	 * @throws Exception
	 *             If there was a problem connecting to the Selenium Server or
	 *             starting the Web Browser.
	 */
	void start(T object) throws Exception;

	/**
	 * Disconnect from the Selenium Server or stop the Web Browser used to
	 * execute the tests.
	 * 
	 * @param object
	 *            The object used to communicate with the Selenium Server or Web
	 *            Browser.
	 * @throws Exception
	 *             If there was a problem disconnecting from the Selenium Server
	 *             or stopping the Web Browser.
	 */
	void stop(T object) throws Exception;
}
