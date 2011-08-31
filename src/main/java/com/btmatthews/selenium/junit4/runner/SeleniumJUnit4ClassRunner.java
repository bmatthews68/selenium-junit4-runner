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

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.Selenium;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class SeleniumJUnit4ClassRunner extends Suite {

	/**
	 * Creates a {@code SeleniumJUnit4ClassRunner} to run the test cases
	 * encapsulated within {@code klass}.
	 * 
	 * @param klass
	 *            The class that encapsulates the test cases.
	 * @throws InitializationError
	 *             If there was an error initialising the class runner.
	 */
	public SeleniumJUnit4ClassRunner(final Class<?> klass)
			throws InitializationError {
		super(klass, buildRunners(klass));
	}

	private static final List<Runner> buildRunners(final Class<?> klass)
			throws InitializationError {
		final List<Runner> runners;
		final SeleniumRCConfiguration seleniumRCConfiguration = klass
				.getAnnotation(SeleniumRCConfiguration.class);
		final WebDriverConfiguration webDriverConfiguration = klass
				.getAnnotation(WebDriverConfiguration.class);
		if (seleniumRCConfiguration == null) {
			if (webDriverConfiguration == null) {
				throw new InitializationError(
						"Annotate test class with either SeleniumRCConfiguration or WebDriverConfiguration");
			} else {
				runners = buildWebDriverRunners(webDriverConfiguration, klass);
			}
		} else {
			if (webDriverConfiguration == null) {
				runners = buildSeleniumRCRunners(seleniumRCConfiguration, klass);
			} else {
				throw new InitializationError(
						"Do not annotate test class with both SeleniumRCConfiguration and WebDriverConfiguration");
			}
		}
		return runners;
	}

	private static final List<Runner> buildWebDriverRunners(
			final WebDriverConfiguration configuration, final Class<?> klass)
			throws InitializationError {
		final List<Runner> runners = new ArrayList<Runner>();
		try {
			for (final Class<? extends WebDriver> webDriverClass : configuration
					.baseDrivers()) {
				final SeleniumAPIFactory apiFactory = new WebDriverAPIFactory(configuration, webDriverClass);
				runners.add(new IndividualSeleniumJUnit4ClassRunner(apiFactory, klass));
			}
		} catch (final Exception e) {
			throw new InitializationError(e);
		}
		return runners;
	}

	private static final List<Runner> buildSeleniumRCRunners(
			final SeleniumRCConfiguration configuration, final Class<?> klass)
			throws InitializationError {
		final List<Runner> runners = new ArrayList<Runner>();
		try {
			for (final String browserStartCommand : configuration
					.browserStartCommands()) {
				final SeleniumAPIFactory factory = new SeleniumRCAPIFactory(
						configuration, browserStartCommand);
				runners.add(new IndividualSeleniumJUnit4ClassRunner(factory,
						klass));
			}
		} catch (final Exception e) {
			throw new InitializationError(e);
		}
		return runners;
	}

	public static class IndividualSeleniumJUnit4ClassRunner extends
			BlockJUnit4ClassRunner {

		private SeleniumAPIFactory seleniumApiFactory;

		public IndividualSeleniumJUnit4ClassRunner(
				final SeleniumAPIFactory apiFactory, final Class<?> klass)
				throws InitializationError {
			super(klass);
			seleniumApiFactory = apiFactory;
		}

		@Override
		protected Object createTest() throws Exception {
			final Selenium seleniumApi = seleniumApiFactory.create();
			final Object test = super.createTest();
			final List<FrameworkField> fields = getTestClass()
					.getAnnotatedFields(SeleniumAPI.class);
			for (final FrameworkField field : fields) {
				field.getField().setAccessible(true);
				field.getField().set(test, seleniumApi);
			}
			return test;
		}

		@Override
		protected Statement classBlock(final RunNotifier notifier) {
			final Statement statement = super.classBlock(notifier);
			return new RunSeleniumBrowser(statement);
		}

		public class RunSeleniumBrowser extends Statement {
			private Statement statementBlock;

			public RunSeleniumBrowser(final Statement block) {
				statementBlock = block;
			}

			public void evaluate() throws Throwable {
		//		seleniumApi.start();
				try {
					statementBlock.evaluate();
				} finally {
			//		seleniumApi.stop();
				}
			}
		}

	}

}
