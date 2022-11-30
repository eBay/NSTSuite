package com.ebay.nst.hosts.manager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class HostsManagerTest {

	@Test(groups = "unitTest")
	public void defaultPoolTypeIsQA() throws Exception {
		assertThat(HostsManager.getInstance().getPoolType(), is(equalTo(PoolType.QA)));
	}
	
	@Test(groups = "unitTest")
	public void getHostForService() throws Exception {
		assertThat(HostsManager.getInstance().getHostForService("checkout"), is(equalTo("https://checkout.test.com")));
	}
	
	@Test(groups = "unitTest", expectedExceptions = NullPointerException.class)
	public void getHostForNullService() throws Exception {
		HostsManager.getInstance().getHostForService(null);
	}
	
	@Test(groups = "unitTest", expectedExceptions = IllegalArgumentException.class)
	public void getHostForUnknownService() throws Exception {
		HostsManager.getInstance().getHostForService("unknown");
	}
}
