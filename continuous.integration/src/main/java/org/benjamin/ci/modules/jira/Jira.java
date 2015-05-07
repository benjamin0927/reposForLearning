package org.benjamin.ci.modules.jira;

import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.BasicStatus;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import com.atlassian.jira.rest.client.JiraRestClient;

public class Jira {
	private final String JIRA_URL = "https://jira.talendforge.org";
	private final String JIRA_ADMIN_USERNAME = "qa-user";
	private final String JIRA_ADMIN_PASSWORD = "qa-user";
	
	private String jiraID;
	private String storyName;
	private String platform;
	private String license;
	private String mode;
	
	private String jiraLink;
	private String status;
	private String summary;
	JiraRestClient client;
	
	public Jira(){
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		URI uri = null;
		try {
			uri = new URI(JIRA_URL);
		} catch (URISyntaxException e) {
			System.err.println("Fails to create URI of " + JIRA_URL + ": "
					+ e.getMessage());
		}
		client = factory.createWithBasicHttpAuthentication(uri,
				JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
	}

	public static void main(String[] args) {
	}

}
