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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.lang.annotation.*;

/**
 * {@code WrappedDriverConfiguration} defines class-level meta-data which can be
 * used to instruct client code with regard to use a wrapped <a
 * href="http://seleniumhq.org">Selenium</a> web driver.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SeleniumConfiguration
public @interface WrappedDriverConfiguration {

    /**
     * The browser URL.
     */
    String browserURL();

    /**
     * The web drivers used to launch the tests. By default we just run with <a
     * href="http://htmlunit.sourceforge.net/">HTML Unit</a>.
     */
    Class<? extends WebDriver>[] baseDrivers() default {HtmlUnitDriver.class};
}
