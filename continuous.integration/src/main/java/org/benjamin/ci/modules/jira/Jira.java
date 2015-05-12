package org.benjamin.ci.modules.jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
//import com.atlassian.jira.rest.client.domain.BasicStatus;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
//import com.atlassian.util.concurrent.Promise;
//import com.atlassian.jira.rest.client.JiraRestClient;

public class Jira {
	public static Logger logger = Logger.getLogger(Jira.class);
	
	private final String JIRA_URL = "";
	private final String JIRA_ADMIN_USERNAME = "";
	private final String JIRA_ADMIN_PASSWORD = "";
	
//	private String jiraId;
//	private String status;
//	private String summary;
	private JiraRestClient client;
	private IssueRestClient issueClient;
	private Map<String, Issue> issueMap = new HashMap<String, Issue>();
	
	public Jira(){
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		URI uri = null;
		try {
			uri = new URI(JIRA_URL);
		} catch (URISyntaxException e) {
			logger.error("Fails to create URI of " + JIRA_URL + ": "
					+ e.getMessage());
		}
		client = factory.createWithBasicHttpAuthentication(uri,
				JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
		issueClient = this.getIssueRestClient();
	}
	
	private IssueRestClient getIssueRestClient(){
		return client.getIssueClient();
	}
	
	public Map<String, Issue> getIssues(){
		return issueMap;
	}
	public Issue getIssueByJiraId(String jiraId) throws InterruptedException, ExecutionException{
		if(issueMap.containsKey(jiraId)) {
			return issueMap.get(jiraId);
		} else {
			Issue issue = issueClient.getIssue(jiraId).get();
			issueMap.put(jiraId, issue);
			return issue;
		}
	}
	
	public Map<String, Issue> getIssuesByJiraIdList(List<String> jiraIds) throws InterruptedException, ExecutionException{
		for(String jiraId : jiraIds) {
			this.getIssueByJiraId(jiraId);
		}
		return issueMap;
	}

	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		logger.info("now - " + now);
		
		Jira jira = new Jira();
		
		for(int i=45800;i<=45835; i++) {
			String jiraId = "QAI-" + i;
			try {
				jira.getIssueByJiraId(jiraId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info(jiraId);
//			logger.info(jiraId + " : Status -" + jira.getStatus(jiraId) + ", Summary - " + jira.getSummary(jiraId));
		}
		
		for(Map.Entry<String, Issue> entry : jira.getIssues().entrySet()){
			logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue().getStatus().getName() + ", "+ entry.getValue().getSummary() );
		}
		
		
		long end = System.currentTimeMillis();
		logger.info("end - " + end);
		
		long duration = end - now;
		logger.error("-------- " + duration);
	}

}
