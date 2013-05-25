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

package com.btmatthews.selenium.junit4.runner;

import com.google.common.base.Supplier;
import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Factory that is responsible for creating the {@link Selenium} instance that
 * wraps a {@link WebDriver} and acting as a an wrapper for the start and stop
 * methods.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class WrappedDriverFactory implements SeleniumFactory<Selenium> {

    /**
     * The configuration annotation.
     */
    private final WrappedDriverConfiguration configuration;

    /**
     * The {@link WebDriver} class.
     */
    private final Class<? extends WebDriver> webDriverClass;

    /**
     * Construct the factory for creating {@link Selenium} instances that wrap
     * web drivers.
     *
     * @param config      The {@link WrappedDriverConfiguration} annotation that
     *                    provides configuration for the test runner.
     * @param driverClass The {@link WebDriver} class.
     */
    public WrappedDriverFactory(final WrappedDriverConfiguration config,
                                final Class<? extends WebDriver> driverClass) {
        configuration = config;
        webDriverClass = driverClass;
    }

    /**
     * Return a string to identify the browser derived from the class name of
     * the underlying web driver.
     *
     * @return The derived browser identification string.
     */
    public String getBrowser() {
        return webDriverClass.getSimpleName();
    }

    /**
     * Create a Selenium Server object that wraps the {@link WebDriver} that is
     * created using reflection. We are explicitly enabling JavaScript for the
     * {@link HtmlUnitDriver} case.
     *
     * @return The new {@link WebDriver} instance.
     */
    public Selenium create() {
        return new WebDriverBackedSelenium(new Supplier<WebDriver>() {
            public WebDriver get() {
                if (HtmlUnitDriver.class.isAssignableFrom(webDriverClass)) {
                    final DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
                    capabilities.setJavascriptEnabled(true);
                    return new HtmlUnitDriver(capabilities);
                } else {
                    try {
                        return webDriverClass.newInstance();
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }, configuration.browserURL());
    }

    /**
     * Start the browser for the wrapped web driver.
     *
     * @param server The Selenium Server wrapping the web driver.
     * @see SeleniumFactory#start(Object)
     */
    public void start(final Selenium server) {
        server.start();
    }

    /**
     * Stop the browser used by the wrapped web driver.
     *
     * @param server The Selenium Server wrapping the web driver.
     * @see SeleniumFactory#stop(Object)
     */
    public void stop(final Selenium server) {
        server.stop();
    }

}
