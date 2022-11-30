package com.ebay.nst.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.schema.validation.GraphQLSchemaValidator;
import com.ebay.service.protocol.http.NSTHttpRequest;

public class NSTGraphQLServiceWrapperTest {

	class TestWrapper implements NSTGraphQLServiceWrapper {

		@Override
		public String getServiceName() {
			return "checkout";
		}

		@Override
		public String getEndpointPath() {
			return "/test";
		}

		@Override
		public GraphQLSchemaValidator getSchemaValidator() {
			return null;
		}

		@Override
		public Map<String, String> getAdditionalHeaders() {
			return null;
		}

		@Override
		public NSTGraphQLRequest getRequest() {
			
			Map<String, Object> variables = new HashMap<>();
			variables.put("one", Integer.valueOf(1));
			variables.put("two", "second");
			
			NSTGraphQLRequest request = new NSTGraphQLRequest("TEST");
			request.setOperationName("operation");
			request.setVariables(variables);
			request.setExtensions("extension");
			return request;
		}
	}
	
	private TestWrapper testWrapper = new TestWrapper();
	
	// Test default implementations
	
	@Test
	public void getRequestType() {
		assertThat(testWrapper.getRequestType(), is(equalTo(NstRequestType.POST)));
	}
	
	@Test
	public void prepareRequest() {
		
		NSTHttpRequest actualRequest = testWrapper.prepareRequest();
		
		Map<String, String> headers = actualRequest.getHeaders();

		assertThat(headers.get("Accept"), is(equalTo("application/graphql-response+json")));
		assertThat(headers.get("Content-Type"), is(equalTo("application/json")));
		assertThat(actualRequest.getRequestType(), is(equalTo(NstRequestType.POST)));
		assertThat(actualRequest.getPayload().toString(), is(equalTo("{\"query\":\"TEST\",\"operationName\":\"operation\",\"variables\":{\"one\":1,\"two\":\"second\"},\"extensions\":\"extension\"}")));
	}
}
