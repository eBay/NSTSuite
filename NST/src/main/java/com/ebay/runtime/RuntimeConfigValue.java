package com.ebay.runtime;

public interface RuntimeConfigValue<T> {

	/**
	 * Get the key for the runtime argument. This is the runtime argument key
	 * entered by the user.
	 * 
	 * @return Runtime argument key.
	 */
	public String getRuntimeArgumentKey();

	/**
	 * Get the runtime config value set after reading the value from the runtime
	 * arguments defined.
	 * 
	 * @return Runtime config value, or null if not set.
	 */
	public T getRuntimeArgumentValue();

	/**
	 * Override the runtime value. Scope of override is within the test case the
	 * override is defined in. Overrides are reset between test cases.
	 * 
	 * @param value Value to override runtime with.
	 * @return Value specified in the override allowing one line to set the override
	 *         and store it locally.
	 */
	public T override(T value);

	/**
	 * Initialize the runtime config value from the runtime argument defined.
	 * 
	 * @param argumentValue Runtime config value. If not specified as a runtime
	 *                      argument, null will be passed in.
	 */
	public void parseRuntimeArgument(String argumentValue);
}
