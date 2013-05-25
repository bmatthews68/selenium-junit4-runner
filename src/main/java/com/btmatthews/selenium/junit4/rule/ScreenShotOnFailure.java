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

package com.btmatthews.selenium.junit4.rule;

import java.io.File;
import java.io.IOException;

import com.btmatthews.selenium.junit4.runner.SeleniumServer;
import com.btmatthews.selenium.junit4.runner.SeleniumWebDriver;
import com.thoughtworks.selenium.Selenium;
import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A method rule that captures screen shots when the test fails.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.5
 */
public class ScreenShotOnFailure extends TestWatcher {

    /**
     * Used to log failure messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScreenShotFilenameGenerator.class);

    /**
     * Injected by the {@link com.btmatthews.selenium.junit4.runner.SeleniumJUnit4ClassRunner} if the Selenium RC
     * API is being used to drive the browser.
     */
    @SuppressWarnings("unused")
    @SeleniumServer
    private Selenium server;

    /**
     * Injected by the {@link com.btmatthews.selenium.junit4.runner.SeleniumJUnit4ClassRunner} if the Selenium Web
     * Driver API is being used to drive the browser.
     */
    @SuppressWarnings("unused")
    @SeleniumWebDriver
    private WebDriver webDriver;

    /**
     * Used to generate unique file names for the screen shot files.
     */
    private final ScreenShotFilenameGenerator generator;

    /**
     * Construct a rule that captures screen shots and uses {@link DefaultScreenShotFilenameGenerator} to generate the
     * file names for the screen shot files.
     *
     * @param screenshotDirectory The directory root where the screen shot files will be generated.
     */
    public ScreenShotOnFailure(final String screenshotDirectory) {
        this(new DefaultScreenShotFilenameGenerator(new File(screenshotDirectory)));
    }

    /**
     * Construct a rule that captures screen shots and uses {@code generator} to generate the file names for the
     * screen shot files.
     *
     * @param generator Generates unique file names for the screen shot files.
     */
    public ScreenShotOnFailure(final ScreenShotFilenameGenerator generator) {
        this.generator = generator;
    }

    /**
     * Handle a test case failure by taking a screen shot from the browser and writing it to a file.
     *
     * @param exception   The exception that describes the test case failure.
     * @param description Describes the unit test that failed.
     */
    @Override
    protected void failed(final Throwable exception,
                          final Description description) {
        try {
            if (webDriver != null) {
                if (webDriver instanceof TakesScreenshot) {
                    final File target = generator.getTargetFilename(description);
                    final byte[] source = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES);
                    FileUtils.forceMkdir(target.getParentFile());
                    FileUtils.writeByteArrayToFile(target, source);
                }
            } else {
                final File target = generator.getTargetFilename(description);
                FileUtils.forceMkdir(target.getParentFile());
                server.captureEntirePageScreenshot(target.getAbsolutePath(), "");
            }
        } catch (final IOException e) {
            LOGGER.error("I/O error capturing screen shot after failure", e);
        }
    }
}
