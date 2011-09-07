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

package com.btmatthews.selenium.junit4.runner.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.btmatthews.selenium.junit4.runner.SeleniumJUnit4ClassRunner;
import com.btmatthews.selenium.junit4.runner.SeleniumServer;
import com.btmatthews.selenium.junit4.runner.ServerConfiguration;
import com.thoughtworks.selenium.Selenium;

/**
 * Unit tests for the {@link ServerConfiguration} configuration style.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@RunWith(SeleniumJUnit4ClassRunner.class)
@ServerConfiguration(browserURL = "http://www.google.com")
public final class ServerITCase {

	/**
	 * The object used to start/stop the server used for testing.
	 */
	@SeleniumServer
	private Selenium server;

	/**
	 * Verify that the test runner injected the Selenium Server.
	 */
	@Test
	public void testInjection() {
		assertNotNull(server);
	}

	/**
	 * Verify that we can navigate to the Google home page.
	 */
	@Test
	public void testHomePage() {
		server.open("/");
		assertEquals("Google", server.getTitle());
	}
}
