/*
 * Copyright 2011-2013 Brian Thomas Matthews
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
import org.openqa.selenium.WebDriver;

import com.btmatthews.selenium.junit4.runner.SeleniumBrowser;
import com.btmatthews.selenium.junit4.runner.SeleniumJUnit4ClassRunner;
import com.btmatthews.selenium.junit4.runner.SeleniumWebDriver;
import com.btmatthews.selenium.junit4.runner.WebDriverConfiguration;

/**
 * Unit tests for the {@link WebDriverConfiguration} configuration style.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
@RunWith(SeleniumJUnit4ClassRunner.class)
@WebDriverConfiguration()
public final class WebDriverITCase {

	/**
	 * The object used to start/stop the web browser used for testing.
	 */
    @SuppressWarnings("unused")
	@SeleniumWebDriver
	private WebDriver webDriver;

    /**
     * The name of the browser being used for the test.
     */
    @SuppressWarnings("unused")
	@SeleniumBrowser
	private String browserName;

	/**
	 * Verify that the test runner injected the web driver.
	 */
	@Test
	public void testInjection() {
		assertNotNull(webDriver);
		assertNotNull(browserName);
		assertEquals("HtmlUnitDriver", browserName);
	}

	/**
	 * Verify that we can navigate to the Google home page.
	 */
	@Test
	public void testHomePage() {
		webDriver.navigate().to("http://www.google.com");
		assertEquals("Google", webDriver.getTitle());
	}
}
