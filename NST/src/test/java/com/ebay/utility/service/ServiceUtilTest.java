package com.ebay.utility.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.nst.rest.NSTRestServiceWrapper;

public class ServiceUtilTest {

	@DataProvider(name = "getUrlDatafunc")
	public Object[][] getUrlDatafunc() {
		return new Object[][] { { "/foo", "https://checkout.test.com/foo" },
				{ "", "https://checkout.test.com" } };
	}

	@Test(groups = "unitTest", dataProvider = "getUrlDatafunc")
	public void testGetUrl(String urlPath, String expectedURL) throws Exception {

		NSTRestServiceWrapper nstServiceWrapper = mock(NSTRestServiceWrapper.class);
		when(nstServiceWrapper.getServiceName()).thenReturn("checkout");
		when(nstServiceWrapper.getEndpointPath()).thenReturn(urlPath);

		URL actualUrl = ServiceUtil.getUrl(nstServiceWrapper);
		assertThat(actualUrl, is(equalTo(new URL(expectedURL))));
	}

	@Test(groups = "unitTest", expectedExceptions = IllegalStateException.class)
	public void testGeturlException() throws Exception {
		NSTRestServiceWrapper nstServiceWrapper = mock(NSTRestServiceWrapper.class);
		when(nstServiceWrapper.getEndpointPath()).thenReturn(null);
		when(nstServiceWrapper.getServiceName()).thenReturn("checkout");

		ServiceUtil.getUrl(nstServiceWrapper);
	}
}
