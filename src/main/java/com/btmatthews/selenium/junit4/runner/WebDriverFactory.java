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

import org.openqa.selenium.WebDriver;

/**
 * Factory that is responsible for creating the {@link WebDriver} instance and
 * acting as a an wrapper for the start and stop methods.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public final class WebDriverFactory implements SeleniumFactory<WebDriver> {

    /**
     * The {@link WebDriver} class.
     */
    private final Class<? extends WebDriver> webDriverClass;

    /**
     * Construct the factory for creating {@link WebDriver} instances.
     *
     * @param driverClass The {@link WebDriver} class.
     */
    public WebDriverFactory(final Class<? extends WebDriver> driverClass) {
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
     * Create an instance of the {@link WebDriver} using reflection.
     *
     * @return The new {@link WebDriver} instance.
     * @throws Exception If the {@link WebDriver} could not be instantiated.
     * @see SeleniumFactory#create()
     */
    public WebDriver create() throws Exception {
        return webDriverClass.newInstance();
    }

    /**
     * This method is not implemented because the {@link WebDriver} was started
     * during instantiation.
     *
     * @param webDriver The {@link WebDriver} instance.
     * @see SeleniumFactory#start(Object)
     */
    public void start(final WebDriver webDriver) {
    }

    /**
     * Stop the {@link WebDriver} by issuing a quit command.
     *
     * @param webDriver The {@link WebDriver} instance.
     * @see SeleniumFactory#stop(Object)
     */
    public void stop(final WebDriver webDriver) {
        webDriver.quit();
    }
}
