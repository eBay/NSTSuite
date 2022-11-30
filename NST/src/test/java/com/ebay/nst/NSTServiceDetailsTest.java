package com.ebay.nst;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

public class NSTServiceDetailsTest {

	@Test(groups = "UnitTest")
	public void nothingSet() {
		NSTServiceDetails details = new NSTServiceDetails();
		String output = details.toString();
		assertThat(output, is(equalTo("")));
	}

	@Test(groups = "UnitTest")
	public void everythingSet() {
		NSTServiceDetails details = new NSTServiceDetails().setCallDescription("Test call")
				.setExternalDocumentation("external doc").setJiraProjectLink("jira project link")
				.setServiceDeveloperContact("developer contact");
		String output = details.toString();
		
		StringBuilder expectedBuilder = new StringBuilder();
		expectedBuilder.append("Service developer contact: developer contact\n");
		expectedBuilder.append("Service JIRA project: jira project link\n");
		expectedBuilder.append("Service call description: Test call\n");
		expectedBuilder.append("Service documentation: external doc\n");
		assertThat(output, is(equalTo(expectedBuilder.toString())));
	}

	@Test(groups = "UnitTest")
	public void onlyServiceDeveloperConactSet() {
		NSTServiceDetails details = new NSTServiceDetails().setServiceDeveloperContact("developer contact");
		String output = details.toString();
		
		StringBuilder expectedBuilder = new StringBuilder();
		expectedBuilder.append("Service developer contact: developer contact\n");
		assertThat(output, is(equalTo(expectedBuilder.toString())));
	}

	@Test(groups = "UnitTest")
	public void onlyJiraProjectLinkSet() {
		NSTServiceDetails details = new NSTServiceDetails().setJiraProjectLink("jira project link");
		String output = details.toString();
		
		StringBuilder expectedBuilder = new StringBuilder();
		expectedBuilder.append("Service JIRA project: jira project link\n");
		assertThat(output, is(equalTo(expectedBuilder.toString())));
	}

	@Test(groups = "UnitTest")
	public void onlyCallDescriptionSet() {
		NSTServiceDetails details = new NSTServiceDetails().setCallDescription("Test call");
		String output = details.toString();
		
		StringBuilder expectedBuilder = new StringBuilder();
		expectedBuilder.append("Service call description: Test call\n");
		assertThat(output, is(equalTo(expectedBuilder.toString())));
	}

	@Test(groups = "UnitTest")
	public void onlyExternalDocumentationSet() {
		NSTServiceDetails details = new NSTServiceDetails().setExternalDocumentation("external doc");
		String output = details.toString();
		
		StringBuilder expectedBuilder = new StringBuilder();
		expectedBuilder.append("Service documentation: external doc\n");
		assertThat(output, is(equalTo(expectedBuilder.toString())));
	}
}
