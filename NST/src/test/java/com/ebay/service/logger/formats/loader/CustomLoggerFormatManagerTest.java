package com.ebay.service.logger.formats.loader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.FormatWriter;
import com.ebay.service.logger.formats.loader.test.classes.all.AndroidWriter;
import com.ebay.service.logger.formats.loader.test.classes.all.IosWriter;
import com.ebay.service.logger.formats.loader.test.classes.duplicateplatform.AndroidOne;
import com.ebay.service.logger.platforms.AndroidLogger;
import com.ebay.service.logger.platforms.HarLogger;
import com.ebay.service.logger.platforms.IosLogger;

public class CustomLoggerFormatManagerTest {
	
	private static final String CUSTOM_LOGGERS_PACKAGE = "customLoggersPackage";

	@BeforeMethod(alwaysRun = true)
	@AfterMethod(alwaysRun = true)
	public void resetBeforeAndAfterEachTest() {
		clearSystemProperties();
	}

	@AfterClass(alwaysRun = true)
	public void resetAtEnd() throws IllegalStateException, URISyntaxException, IOException {
		clearSystemProperties();
		RuntimeConfigManager.getInstance().reinitialize();
	}
	
	private void clearSystemProperties() {
		System.clearProperty(CUSTOM_LOGGERS_PACKAGE);
	}
	
	// ------------------------------------
	// Tests
	// ------------------------------------
	
	@Test
	public void defaultLoggers() {
		
		RuntimeConfigManager.getInstance().reinitialize();
		Map<Platform, FormatWriter> loggers = CustomLoggerFormatManager.getInstance().reinitialize().getPlatformLoggers();
		assertThat(loggers.size(), is(equalTo(4)));
		assertThat(loggers.get(Platform.SITE), is(instanceOf(HarLogger.class)));
		assertThat(loggers.get(Platform.MWEB), is(instanceOf(HarLogger.class)));
		assertThat(loggers.get(Platform.ANDROID), is(instanceOf(AndroidLogger.class)));
		assertThat(loggers.get(Platform.IOS), is(instanceOf(IosLogger.class)));
	}

	@Test
	public void loadCustomLoggers() {
		
		System.setProperty(CUSTOM_LOGGERS_PACKAGE, "com.ebay.service.logger.formats.loader.test.classes.all");
		RuntimeConfigManager.getInstance().reinitialize();
		Map<Platform, FormatWriter> loggers = CustomLoggerFormatManager.getInstance().reinitialize().getPlatformLoggers();
		assertThat(loggers.get(Platform.ANDROID), is(instanceOf(AndroidWriter.class)));
		assertThat(loggers.get(Platform.IOS), is(instanceOf(IosWriter.class)));
		assertThat(loggers.get(Platform.MWEB), is(instanceOf(HarLogger.class)));
		assertThat(loggers.get(Platform.SITE), is(instanceOf(HarLogger.class)));
	}
	
	@Test
	public void loadSamePlatformLoggerTwice() {
		
		System.setProperty(CUSTOM_LOGGERS_PACKAGE, "com.ebay.service.logger.formats.loader.test.classes.duplicateplatform");
		RuntimeConfigManager.getInstance().reinitialize();
		Map<Platform, FormatWriter> loggers = CustomLoggerFormatManager.getInstance().reinitialize().getPlatformLoggers();
		assertThat(loggers.get(Platform.ANDROID), is(instanceOf(AndroidOne.class)));
		assertThat(loggers.get(Platform.IOS), is(instanceOf(IosLogger.class)));
		assertThat(loggers.get(Platform.MWEB), is(instanceOf(HarLogger.class)));
		assertThat(loggers.get(Platform.SITE), is(instanceOf(HarLogger.class)));
	}
}
