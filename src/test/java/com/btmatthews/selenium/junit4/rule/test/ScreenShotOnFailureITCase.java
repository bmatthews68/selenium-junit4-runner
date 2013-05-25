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

package com.btmatthews.selenium.junit4.rule.test;

import com.btmatthews.selenium.junit4.rule.ScreenShotOnFailure;
import com.btmatthews.selenium.junit4.runner.SeleniumJUnit4ClassRunner;
import com.btmatthews.selenium.junit4.runner.SeleniumWebDriver;
import com.btmatthews.selenium.junit4.runner.WebDriverConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertEquals;

/**
 * Integration test cases for the {@link ScreenShotOnFailure} rule.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.5
 */
@RunWith(SeleniumJUnit4ClassRunner.class)
@WebDriverConfiguration(baseDrivers = FirefoxDriver.class)
public class ScreenShotOnFailureITCase {

    /**
     * This run dumps screen shots to the target/screenshots directory when the test case fails.
     */
    @Rule
    public ScreenShotOnFailure screenShotRule = new ScreenShotOnFailure("target/screenshots");
    /**
     * The object used to start/stop the web browser used for testing.
     */
    @SuppressWarnings("unused")
    @SeleniumWebDriver
    private WebDriver webDriver;

    /**
     * This test case always fails triggering the {@link #screenShotRule} rule which will take a screen shot and
     * write it to the target/screenshots directory.
     */
    @Test
    public void testRule() {
        webDriver.navigate().to("http://www.google.com");
        assertEquals("Google", webDriver.getTitle());
    }
}
