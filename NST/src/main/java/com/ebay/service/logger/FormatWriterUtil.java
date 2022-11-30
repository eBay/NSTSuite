package com.ebay.service.logger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

import org.testng.annotations.Test;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpResponse;
import com.ebay.service.protocol.http.NSTHttpResponseImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FormatWriterUtil {

	/**
	 * Get the class and method name of the TestNG test that is running.
	 *
	 * @return Class and method name instance, or null if there was an issue.
	 */
	public static ClassAndMethodName getClassAndMethodName() {

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		for (int i = 1; i < stackTrace.length; i++) {

			StackTraceElement traceElement = stackTrace[i];
			String className = traceElement.getClassName();
			String methodName = traceElement.getMethodName();

			Class<?> classMatch;
			try {
				classMatch = Class.forName(className);
			} catch (ClassNotFoundException e) {
				return null;
			}

			Method[] methods = classMatch.getMethods();
			boolean matched = false;
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Annotation[] annotations = method.getAnnotations();
					for (Annotation annotation : annotations) {
						if (annotation instanceof Test) {
							matched = true;
							break;
						}
					}
					if (matched) {
						break;
					}
				}
			}

			if (!matched) {
				continue;
			}

			className = className.substring(className.lastIndexOf(".") + 1);
			return new ClassAndMethodName(className, methodName);
		}

		return null;
	}

	/**
	 * Get the file name to use when writing request data to file. names. DOES NOT
	 * INCLUDE EXTENSION.
	 *
	 * @param className          Name of the class containing the test being
	 *                           executed.
	 * @param methodName         Name of the test method being executed.
	 * @param index              Call sequence order.
	 * @param serviceWrapperName Name of the service wrapper used to make the call.
	 * @return File name
	 */
	public static String getFileName(String className, String methodName, int index, String serviceWrapperName) {
		Objects.requireNonNull(className, "Class name MUST NOT be null.");
		Objects.requireNonNull(methodName, "Method name MUST NOT be null.");
		Objects.requireNonNull(serviceWrapperName, "Service wrapper name MUST NOT be null.");
		return String.format("%s_%s_%d_%s", className, methodName, index, serviceWrapperName);
	}

	/**
	 * Get the mock folder and file name in a single path for finding mock files to
	 * generate/replace. DOES NOT INCLUDE EXTENSION.
	 * 
	 * @param className          Class name of the class containing the currently
	 *                           executing test method.
	 * @param methodName         Name of the currently executing test method.
	 * @param index              Call sequence order.
	 * @param serviceWrapperName Name of the service wrapper used to make the call.
	 * @return File name
	 */
	public static String getMockFolderAndFileName(String className, String methodName, int index,
			String serviceWrapperName) {
		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		String outputFolder;
		switch (platform) {
		case ANDROID:
			outputFolder = RuntimeConfigManager.getInstance().getAndroidMocksLocation();
			break;
		case IOS:
			outputFolder = RuntimeConfigManager.getInstance().getIosMocksLocation();
			break;
		case MWEB:
		case SITE:
		default:
			throw new IllegalStateException(String.format("Platform %s is unsupported.", platform.name()));
		}

		if (outputFolder == null) {
			throw new IllegalStateException(String.format(
					"Mock output folder for %s is undefined. Please define an output path for mocks using the available runtime arguments.",
					platform.name()));
		}

		return getOutputFolderAndFileName(outputFolder, className, methodName, index, serviceWrapperName);
	}

	/**
	 * Get the test folder and file name in a single path for finding test files to
	 * generate/update statements for. DOES NOT INCLUDE EXTENSION.
	 * 
	 * @param className          Class name of the class containing the currently
	 *                           executing test method.
	 * @param methodName         Name of the currently executing test method.
	 * @param index              Call sequence order.
	 * @param serviceWrapperName Name of the service wrapper used to make the call.
	 * @return File name
	 */
	public static String getTestFolderAndFileName(String className, String methodName, int index,
			String serviceWrapperName) {

		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		String outputFolder;
		switch (platform) {
		case ANDROID:
			outputFolder = RuntimeConfigManager.getInstance().getAndroidTestsLocation();
			break;
		case IOS:
			outputFolder = RuntimeConfigManager.getInstance().getIosTestsLocation();
			break;
		case MWEB:
		case SITE:
		default:
			throw new IllegalStateException(String.format("Platform %s is unsupported.", platform.name()));
		}

		if (outputFolder == null) {
			throw new IllegalStateException(String.format(
					"Test output folder for %s is undefined. Please define an output path for tests using the available runtime arguments.",
					platform.name()));
		}

		return getOutputFolderAndFileName(outputFolder, className, methodName, index, serviceWrapperName);
	}

	/**
	 * Get the output folder and file name (without extension). DOES NOT INCLUDE
	 * EXTENSION.
	 *
	 * @param outputFolder       Folder path for outputting file to.
	 * @param className          Name of the class containing the test being
	 *                           executed.
	 * @param methodName         Name of the test method being executed.
	 * @param index              Call sequence order.
	 * @param serviceWrapperName Name of the service wrapper used to make the call.
	 * @return File name
	 */
	protected static String getOutputFolderAndFileName(String outputFolder, String className, String methodName,
			int index, String serviceWrapperName) {
		
		Objects.requireNonNull(outputFolder, "Output folder MUST NOT be null.");

		return String.format("%s%s%s", outputFolder, File.separator,
				getFileName(className, methodName, index, serviceWrapperName));
	}

	/**
	 * Remove mock files associated with the class and method name specified from
	 * disk. DOES NOT INCLUDE EXTENSION.
	 * 
	 * @param className  Name of the test class containing the currently executing
	 *                   test method.
	 * @param methodName Name of the currently executing test method.
	 */
	public static void removeMockFilesMatchingClassAndMethodName(String className, String methodName) {

		Platform platform = RuntimeConfigManager.getInstance().getPlatform();
		String outputFolder;
		switch (platform) {
		case ANDROID:
			outputFolder = RuntimeConfigManager.getInstance().getAndroidMocksLocation();
			break;
		case IOS:
			outputFolder = RuntimeConfigManager.getInstance().getIosMocksLocation();
			break;
		case MWEB:
		case SITE:
		default:
			throw new IllegalStateException(String.format("Platform %s is unsupported.", platform.name()));
		}

		if (outputFolder == null) {
			throw new IllegalStateException(String.format(
					"Mock output folder for %s is undefined. Please define an output path for mocks using the available runtime arguments.",
					platform.name()));
		}

		removeFileFromOutputDirectory(outputFolder, className, methodName);
	}

	/**
	 * Remove the files from the output directory for the class name and test method
	 * being executed. Extension is NOT considered when removing files.
	 *
	 * @param outputFolderPath Path to the output folder to clean up.
	 * @param className        The name of the test class.
	 * @param methodName       The name of the test method.
	 * @throws Exception Exception thrown.
	 */
	private static void removeFileFromOutputDirectory(String outputFolderPath, String className, String methodName) {

		Objects.requireNonNull(outputFolderPath, "Output folder MUST NOT be null.");
		Objects.requireNonNull(className, "Class name MUST NOT be null.");
		Objects.requireNonNull(methodName, "Method name MUST NOT be null.");

		File outputDirectory = new File(outputFolderPath);
		if (!outputDirectory.exists()) {
			return;
		}

		String filePrefixToMatch = String.format("%s_%s", className, methodName);

		File[] files = outputDirectory.listFiles();
		for (File file : files) {

			if (file.getName().startsWith(filePrefixToMatch)) {
				file.delete();
			}
		}
	}

	/**
	 * Get the modified response payload with the specified injection applied.
	 * 
	 * @param response         Response to modify.
	 * @param responseInjector Injector to use when modifying the response.
	 * @return Cloned response model (the response parameter is NOT modified), or
	 *         null if input response was null.
	 */
	public static NSTHttpResponse getModifiedResponsePayload(NSTHttpResponse response,
			ResponseLoggerInjector responseInjector) {

		if (response == null) {
			return null;
		}

		// Apply pretty print formatting for benefit of service processor.
		String jsonString = response.getPayload();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(jsonString);
		String modifiedResponse = gson.toJson(jsonElement);

		if (responseInjector != null) {
			modifiedResponse = responseInjector.processServiceResponse(modifiedResponse);
		}

		NSTHttpResponseImpl clone = new NSTHttpResponseImpl();
		clone.setPayload(modifiedResponse);
		clone.setResponseCode(response.getResponseCode());
		clone.setHeaders(response.getHeaders());

		return clone;
	}
}
