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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code ServerConfiguration} defines class-level meta-data which can be used
 * to instruct client code with regard to use a <a
 * href="http://seleniumhq.org">Selenium</a> server.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SeleniumConfiguration
public @interface ServerConfiguration {

	/**
	 * The host on which the Selenium Server resides. The default value is
	 * {@link Constants#SELENIUM_SERVER_HOST}.
	 */
	String serverHost() default Constants.SELENIUM_SERVER_HOST;

	/**
	 * The port on which the Selenium Server is listening. The default value is
	 * {@link Constants#SELENIUM_SERVER_PORT}.
	 */
	int serverPort() default Constants.SELENIUM_SERVER_PORT;

	/**
	 * The browser URL.
	 */
	String browserURL();

	/**
	 * The browser start commands used to launch the tests. The default value is
	 * {@link Constants#DEFAULT_START_COMMAND}.
	 */
	String[] browserStartCommands() default { Constants.DEFAULT_START_COMMAND };
}
