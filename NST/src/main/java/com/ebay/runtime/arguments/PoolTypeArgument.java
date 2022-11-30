package com.ebay.runtime.arguments;

import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.nst.hosts.manager.PoolType;
import com.ebay.runtime.RuntimeConfigValue;

public class PoolTypeArgument implements RuntimeConfigValue<PoolType> {

	public static final String KEY = "testExeEnv";
	private PoolType poolType = PoolType.QA;

	@Override
	public String getRuntimeArgumentKey() {
		return KEY;
	}

	@Override
	public PoolType getRuntimeArgumentValue() {
		return poolType;
	}

	@Override
	public void parseRuntimeArgument(String argumentValue) {

		// We don't parse the argument value. Instead, we get this from the
		// HostsManager.
		try {
			poolType = HostsManager.getInstance().getPoolType();
		} catch (Throwable e) {
			e.printStackTrace();
			poolType = PoolType.QA;
		}
	}

	@Override
	public PoolType override(PoolType value) {
		poolType = value;
		return poolType;
	}

}
