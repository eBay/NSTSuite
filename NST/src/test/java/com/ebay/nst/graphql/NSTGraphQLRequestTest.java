package com.ebay.nst.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class NSTGraphQLRequestTest {
	
	@Test(groups = "unitTest")
	public void initializeWithNullQuery() {
		NSTGraphQLRequest request = new NSTGraphQLRequest(null);
		assertThat(request.getQuery(), is(nullValue()));
	}
	
	@Test(groups = "unitTest")
	public void initializeWithValidQuery() {
		NSTGraphQLRequest request = new NSTGraphQLRequest("foo");
		assertThat(request.getQuery(), is(equalTo("foo")));
	}

	@Test(groups = "unitTest")
	public void setVariablesToNull() {
		NSTGraphQLRequest request = new NSTGraphQLRequest(null);
		assertThat(request.getVariables(), is(nullValue()));
		
		Map<String, Object> expectedMap = new HashMap<>();
		expectedMap.put("key", "value");
		request.setVariables(expectedMap);
		assertThat(request.getVariables(), is(equalTo(expectedMap)));
		
		request.setVariables(null);
		assertThat(request.getVariables(), is(nullValue()));
	}
	
	@Test(groups = "unitTest")
	public void initializeVariablesWithAddVariable() {
		NSTGraphQLRequest request = new NSTGraphQLRequest(null);
		assertThat(request.getVariables(), is(nullValue()));
		
		request.addVariable("key", "value");
		
		Map<String, Object> expectedMap = new HashMap<>();
		expectedMap.put("key", "value");
		assertThat(request.getVariables(), is(equalTo(expectedMap)));
	}
}
