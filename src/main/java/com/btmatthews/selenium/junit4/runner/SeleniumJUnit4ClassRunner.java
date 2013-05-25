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

import com.thoughtworks.selenium.Selenium;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A test runner that runs a test case as a suite of tests.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.0
 */
public class SeleniumJUnit4ClassRunner extends Suite {

    /**
     * Creates a {@code SeleniumJUnit4ClassRunner} to run the test cases
     * encapsulated within {@code klass}.
     *
     * @param klass The class that encapsulates the test cases.
     * @throws InitializationError If there was an error initialising the test runner.
     */
    public SeleniumJUnit4ClassRunner(final Class<?> klass)
            throws InitializationError {
        super(klass, buildRunners(klass));
    }

    /**
     * Build the test runners for each browser. The test class must have been
     * annotated with {@link ServerConfiguration} or
     * {@link WebDriverConfiguration} to provide the configuration.
     *
     * @param klass The test class.
     * @return A list of {@link Runner} objects.
     * @throws InitializationError If there was an error initialising the test runners.
     */
    private static List<Runner> buildRunners(final Class<?> klass)
            throws InitializationError {
        final List<Runner> runners;
        final ServerConfiguration seleniumServerConfiguration = klass
                .getAnnotation(ServerConfiguration.class);
        final WebDriverConfiguration webDriverConfiguration = klass
                .getAnnotation(WebDriverConfiguration.class);
        final WrappedDriverConfiguration wrappedDriverConfiguration = klass
                .getAnnotation(WrappedDriverConfiguration.class);
        if (seleniumServerConfiguration != null) {
            runners = buildSeleniumServerRunners(seleniumServerConfiguration,
                    klass);
        } else if (webDriverConfiguration != null) {
            runners = buildWebDriverRunners(webDriverConfiguration, klass);
        } else if (wrappedDriverConfiguration != null) {
            runners = buildWrappedDriverRunners(wrappedDriverConfiguration,
                    klass);
        } else {
            throw new InitializationError(
                    "Annotate test class with either ServerConfiguration, WebDriverConfiguration or WrappedDriverConfiguration");
        }
        return runners;
    }

    /**
     * Build the test runners for each browser for test cases that were
     * annotated with {@link WebDriverConfiguration} to provide the
     * configuration. A test runner is created for each web driver specified by
     * {@link WebDriverConfiguration#baseDrivers()}.
     *
     * @param configuration The {@link WebDriverConfiguration} annotation.
     * @param klass         The test class.
     * @return A list of {@link Runner} test runners.
     * @throws InitializationError If there was an error initialising the test runners.
     */
    private static List<Runner> buildWebDriverRunners(
            final WebDriverConfiguration configuration, final Class<?> klass)
            throws InitializationError {
        final List<Runner> runners = new ArrayList<Runner>();
        try {
            for (final Class<? extends WebDriver> webDriverClass : configuration
                    .baseDrivers()) {
                final WebDriverFactory factory = new WebDriverFactory(webDriverClass);
                runners.add(new SeleniumWebDriverJUnit4ClassRunner(factory,
                        klass));
            }
        } catch (final Exception e) {
            throw new InitializationError(e);
        }
        return runners;
    }

    /**
     * Build the test runners for each browser for test cases that were
     * annotated with {@link WrappedDriverConfiguration} to provide the
     * configuration. A test runner is created for each web driver specified by
     * {@link WrappedDriverConfiguration#baseDrivers()}.
     *
     * @param configuration The {@link WrappedDriverConfiguration} annotation.
     * @param klass         The test class.
     * @return A list of {@link Runner} test runners.
     * @throws InitializationError If there was an error initialising the test runners.
     */
    private static List<Runner> buildWrappedDriverRunners(
            final WrappedDriverConfiguration configuration, final Class<?> klass)
            throws InitializationError {
        final List<Runner> runners = new ArrayList<Runner>();
        try {
            for (final Class<? extends WebDriver> webDriverClass : configuration
                    .baseDrivers()) {
                final WrappedDriverFactory factory = new WrappedDriverFactory(
                        configuration, webDriverClass);
                runners.add(new SeleniumServerJUnit4ClassRunner(factory, klass));
            }
        } catch (final Exception e) {
            throw new InitializationError(e);
        }
        return runners;
    }

    /**
     * Build the test runners for each browser for test cases that were
     * annotated with {@link ServerConfiguration} to provide the configuration.
     * A test runner is created for each browser start command specified by
     * {@link ServerConfiguration#browserStartCommands()}.
     *
     * @param configuration The {@link ServerConfiguration} annotation.
     * @param klass         The test class.
     * @return A list of {@link Runner} test runners.
     * @throws InitializationError If there was an error initialising the test runners.
     */
    private static List<Runner> buildSeleniumServerRunners(
            final ServerConfiguration configuration, final Class<?> klass)
            throws InitializationError {
        final List<Runner> runners = new ArrayList<Runner>();
        try {
            for (final String browserStartCommand : configuration
                    .browserStartCommands()) {
                final ServerFactory factory = new ServerFactory(configuration,
                        browserStartCommand);
                runners.add(new SeleniumServerJUnit4ClassRunner(factory, klass));
            }
        } catch (final Exception e) {
            throw new InitializationError(e);
        }
        return runners;
    }

    /**
     * An abstract test runner that implements the
     * {@link Runner#run(RunNotifier)} and {@link org.junit.runners.ParentRunner<T>#createTest() ParentRunner<T>#createTest()}
     * methods generically.
     *
     * @param <T> <ul>
     *            <li>{@link Selenium} for tests that use the Selenium 1.0 API</li>
     *            <li>{@link WebDriver} for tests that use the Selenium 2.0 API</li>
     *            </ul>
     * @param <A> <ul>
     *            <li>{@link SeleniumServer} for tests that use the Selenium 1.0
     *            API</li>
     *            <li>{@link SeleniumWebDriver} for tests that use the Selenium
     *            2.0 API</li>
     *            </ul>
     */
    abstract static class AbstractSeleniumJUnit4ClassRunner<T, A extends Annotation>
            extends BlockJUnit4ClassRunner {

        /**
         * The Selenium object.
         */
        private T selenium;

        /**
         * The factory used to create, start and stop the Selenium object.
         */
        private SeleniumFactory<T> seleniumFactory;

        /**
         * The annotation type which will be used to identified fields in test
         * objects and rules that are to be injected with the Selenium server or
         * web driver.
         */
        private Class<A> annotationType;

        /**
         * Construct a test runner that will run the tests for a specific
         * browser instance using a Selenium server or web driver..
         *
         * @param factory The factory used to create, start and stop the Selenium
         *                server or web driver.
         * @param type    The annotation type which will be used to identified
         *                fields in test objects and rules that are to be injected
         *                with the Selenium server or web driver.
         * @param klass   The test class.
         * @throws InitializationError If there was a problem constructing the test runner.
         */
        public AbstractSeleniumJUnit4ClassRunner(
                final SeleniumFactory<T> factory, final Class<A> type,
                final Class<?> klass) throws InitializationError {
            super(klass);
            seleniumFactory = factory;
            annotationType = type;
        }

        @Override
        public void run(final RunNotifier notifier) {
            try {
                selenium = seleniumFactory.create();
                seleniumFactory.start(selenium);
                try {
                    super.run(notifier);
                } finally {
                    seleniumFactory.stop(selenium);
                }
            } catch (Throwable e) {
                final Failure failure = new Failure(getDescription(), e);
                notifier.fireTestFailure(failure);
            } finally {
                selenium = null;
            }
        }

        /**
         * Create the test object and inject Selenium server or web driver into
         * fields that were annotated with {@code annotationType}.
         *
         * @return The test object.
         * @throws Exception If there was an error creating the test object.
         */
        @Override
        protected Object createTest() throws Exception {
            final Object test = super.createTest();
            final TestClass testClass = getTestClass();
            final String browser = seleniumFactory.getBrowser();

            List<FrameworkField> fields = testClass
                    .getAnnotatedFields(annotationType);
            for (final FrameworkField field : fields) {
                FieldUtils.writeField(field.getField(), test, selenium, true);
            }

            fields = testClass.getAnnotatedFields(SeleniumBrowser.class);
            for (final FrameworkField field : fields) {
                FieldUtils.writeField(field.getField(), test, browser, true);
            }

            final List<TestRule> rules = this.getTestRules(test);
            for (final TestRule rule : rules) {
                final Field[] ruleFields = rule.getClass().getDeclaredFields();
                for (final Field ruleField : ruleFields) {
                    if (ruleField.getAnnotation(annotationType) != null) {
                        FieldUtils.writeField(ruleField, rule, selenium, true);
                    } else if (ruleField.getAnnotation(SeleniumBrowser.class) != null) {
                        FieldUtils.writeField(ruleField, rule, browser, true);
                    }
                }
            }
            return test;
        }
    }

    /**
     * A test runner that will run tests for a specific browser instance using
     * the Selenium server interface. This Selenium server may actually be
     * wrapping a web driver.
     */
    static class SeleniumServerJUnit4ClassRunner extends
            AbstractSeleniumJUnit4ClassRunner<Selenium, SeleniumServer> {

        /**
         * Construct a test runner that will run the tests for a specific
         * browser instance using a Selenium server. The Selenium server may be
         * wrapping a web driver.
         *
         * @param factory The factory used to create, start and stop the Selenium
         *                server.
         * @param klass   The test class.
         * @throws InitializationError If there was a problem constructing the test runner.
         */
        public SeleniumServerJUnit4ClassRunner(
                final SeleniumFactory<Selenium> factory, final Class<?> klass)
                throws InitializationError {
            super(factory, SeleniumServer.class, klass);
        }
    }

    /**
     * A test runner that will run the tests for a specific browser instance
     * using a Selenium web driver.
     */
    static class SeleniumWebDriverJUnit4ClassRunner extends
            AbstractSeleniumJUnit4ClassRunner<WebDriver, SeleniumWebDriver> {

        /**
         * Construct a test runner that will run the tests for a specific
         * browser instance using a Selenium web driver.
         *
         * @param factory The factory used to create, start and stop the Selenium
         *                web driver.
         * @param klass   The test class.
         * @throws InitializationError If there was a problem constructing the test runner.
         */
        public SeleniumWebDriverJUnit4ClassRunner(
                final WebDriverFactory factory, final Class<?> klass)
                throws InitializationError {
            super(factory, SeleniumWebDriver.class, klass);
        }
    }
}
