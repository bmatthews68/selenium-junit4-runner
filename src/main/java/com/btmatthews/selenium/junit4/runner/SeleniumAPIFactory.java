package com.btmatthews.selenium.junit4.runner;

import com.thoughtworks.selenium.Selenium;

public interface SeleniumAPIFactory {

	Selenium create() throws Exception;
	
	void start(Selenium api) throws Exception;

	void stop(Selenium api) throws Exception;
}
