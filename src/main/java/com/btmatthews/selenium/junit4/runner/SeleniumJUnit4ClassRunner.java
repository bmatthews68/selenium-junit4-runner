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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.Selenium;

/**
 * A test runner that runs a test case as a suite of tests.
 * 
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
	 *             If there was an error initialising the test runner.
	 */
	public SeleniumJUnit4ClassRunner(final Class<?> klass)
			throws InitializationError {
		super(klass, buildRunners(klass));
	}

	/**
	 * Build the test runners for each browser. The test class must have been
	 * annotated with {@link SeleniumRCConfiguration} or
	 * {@link WebDriverConfiguration} to provide the configuration.
	 * 
	 * @param klass
	 *            The test class.
	 * @return A list of {@link Runner} objects.
	 * @throws InitializationError
	 *             If there was an error initialising the test runners.
	 */
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

	/**
	 * Build the test runners for each browser for test cases that were
	 * annotated with {@link WebDriverConfiguration} to provide the
	 * configuration. A test runner is created for each web driver specified by
	 * {@link WebDriverConfiguration#baseDrivers()}.
	 * 
	 * @param configuration
	 *            The {@link WebDriverConfiguration} annotation.
	 * @param klass
	 *            The test class.
	 * @return A list of {@link Runner} test runners.
	 * @throws InitializationError
	 */
	private static final List<Runner> buildWebDriverRunners(
			final WebDriverConfiguration configuration, final Class<?> klass)
			throws InitializationError {
		final List<Runner> runners = new ArrayList<Runner>();
		try {
			for (final Class<? extends WebDriver> webDriverClass : configuration
					.baseDrivers()) {
				final SeleniumAPIFactory apiFactory = new WebDriverAPIFactory(
						configuration, webDriverClass);
				runners.add(new IndividualSeleniumJUnit4ClassRunner(apiFactory,
						klass));
			}
		} catch (final Exception e) {
			throw new InitializationError(e);
		}
		return runners;
	}

	/**
	 * Build the test runners for each browser for test cases that were
	 * annotated with {@link SeleniumRCConfiguration} to provide the
	 * configuration. A test runner is created for each browser start command
	 * specified by {@link SeleniumRCConfiguration#browserStartCommands()}.
	 * 
	 * @param configuration
	 *            The {@link SeleniumRCConfiguration} annotation.
	 * @param klass
	 *            The test class.
	 * @return A list of {@link Runner} test runners.
	 * @throws InitializationError
	 */
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

		/**
		 * The factory used to create, start and stop the Selenium API object
		 */
		private SeleniumAPIFactory seleniumApiFactory;

		/**
		 * Construct a test runner that will run the tests for a specific
		 * browser instance.
		 * 
		 * @param apiFactory
		 *            The factory used to create, start and stop the Selenium
		 *            API object.
		 * @param klass
		 *            The test class.
		 * @throws InitializationError
		 *             If there was a problem constructing the test runner.
		 */
		public IndividualSeleniumJUnit4ClassRunner(
				final SeleniumAPIFactory apiFactory, final Class<?> klass)
				throws InitializationError {
			super(klass);
			seleniumApiFactory = apiFactory;
		}

		/**
		 * Create the test object and inject Selenium API object to fields that
		 * were annotated with {@link SeleniumAPI}.
		 * 
		 * @return The test object.
		 * @throws Exception
		 *             If there was an error creating the test object.
		 */
		@Override
		protected Object createTest() throws Exception {
			final Selenium seleniumApi = seleniumApiFactory.create();
			final Object test = super.createTest();
			final TestClass testClass = getTestClass();
			final List<FrameworkField> fields = testClass
					.getAnnotatedFields(SeleniumAPI.class);
			for (final FrameworkField field : fields) {
				FieldUtils
						.writeField(field.getField(), test, seleniumApi, true);
			}

			// TODO Move this code to where it will get executed at the start of
			// each test because rules is coming back empty

			final List<MethodRule> rules = testClass.getAnnotatedFieldValues(
					test, Rule.class, MethodRule.class);
			for (final MethodRule rule : rules) {
				final Field[] ruleFields = rule.getClass().getDeclaredFields();
				for (final Field ruleField : ruleFields) {
					if (ruleField.getAnnotation(SeleniumAPI.class) != null) {
						FieldUtils.writeField(ruleField, rule, seleniumApi,
								true);
					}
				}
			}
			return test;
		}

		@Override
		protected Statement classBlock(final RunNotifier notifier) {
			final Statement statement = super.classBlock(notifier);
			return new RunSeleniumBrowser(statement);
		}

		// TODO Figure out how to get the seleniumApi in here. We need
		// start/stop to work for Selenium RC.
		
		public class RunSeleniumBrowser extends Statement {
			private Statement statementBlock;

			public RunSeleniumBrowser(final Statement block) {
				statementBlock = block;
			}

			public void evaluate() throws Throwable {
				// seleniumApi.start();
				try {
					statementBlock.evaluate();
				} finally {
					// seleniumApi.stop();
				}
			}
		}

	}

}
