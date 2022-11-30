package com.ebay.nst;

public class NSTServiceDetails {

	private String serviceDeveloperContact;
	private String jiraProjectLink;
	private String callDescription;
	private String externalDocumentation;

	public NSTServiceDetails setServiceDeveloperContact(String serviceDeveloperContact) {
		this.serviceDeveloperContact = serviceDeveloperContact;
		return this;
	}

	public NSTServiceDetails setJiraProjectLink(String jiraProjectLink) {
		this.jiraProjectLink = jiraProjectLink;
		return this;
	}

	public NSTServiceDetails setCallDescription(String callDescription) {
		this.callDescription = callDescription;
		return this;
	}

	public NSTServiceDetails setExternalDocumentation(String externalDocumentation) {
		this.externalDocumentation = externalDocumentation;
		return this;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		if (serviceDeveloperContact != null) {
			builder.append("Service developer contact: " + serviceDeveloperContact);
			builder.append("\n");
		}

		if (jiraProjectLink != null) {
			builder.append("Service JIRA project: " + jiraProjectLink);
			builder.append("\n");
		}

		if (callDescription != null) {
			builder.append("Service call description: " + callDescription);
			builder.append("\n");
		}

		if (externalDocumentation != null) {
			builder.append("Service documentation: " + externalDocumentation);
			builder.append("\n");
		}

		return builder.toString();
	}
}
